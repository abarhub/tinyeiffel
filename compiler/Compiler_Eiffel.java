package tinyeiffel.compiler;

import java.io.*;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamRecognitionException;
import tinyeiffel.ast.*;

import java.util.*;
import tinyeiffel.erreur.*;
import tinyeiffel.lace.*;
import tinyeiffel.middle.Convertion;

import java.util.logging.*;
import tinyeiffel.interpreteur.*;
import tinyeiffel.interpreteur2.Interpreteur2;
import tinyeiffel.genere_c.*;
import tinyeiffel.genere_java.ConvertieJava;
import tinyeiffel.genere_java.ConvertieJava2;
import tinyeiffel.verification.*;
import tinyeiffel.intermediaire.*;

/*
 * a faire: - Loggin
 * - CVS
 */

//coucou
public class Compiler_Eiffel {
	public Vector liste_classe = new Vector();

	public Vector liste_type = new Vector();

	public boolean table_heritage_directe[][], table_heritage[][];

	public int classes_traite = -1;

	public Logging logging;

	public Context context;

	public Type entier, tab_entier, bool, car, reel, dbl, str;

	public Logger logger,profiler;

	public FileHandler fh,fh2;

	protected boolean initialise;

	public tinyeiffel.intermediaire.CIProgramme programme;
	
	public Vector verifications_classe;
	
	public VerificationsGlobales verif_globale;
		
	private final boolean incremental;

	public Compiler_Eiffel() {
		incremental=false;
		logging = new Logging();
		init();
	}

	public Compiler_Eiffel(String nom_fichier,String chemin[]) {
		incremental=false;
		logging = new Logging();
		init();
		compile(nom_fichier,chemin);
	}

	public Compiler_Eiffel(String nom_fichier, int no,String chemin[]) {
		incremental=false;
		logging = new Logging();
		stop = no;
		init();
		compile(nom_fichier,chemin);
	}


	public Compiler_Eiffel(String nom_fichier, int no,
			boolean incremental,String chemin[]) {
		this.incremental=incremental;
		logging = new Logging();
		stop = no;
		init();
		compile(nom_fichier,chemin);
	}
	
	public Compiler_Eiffel(int no) {
		logging = new Logging();
		stop = no;
		incremental=false;
		init();
	}

	//protected boolean no_file=true;
	protected void init() {
		assert (!initialise);
		init_profile();
		init_logger();
		initialise = true;
	}

	/**
	 * 
	 */
	protected void init_logger() {
		boolean trouve;
		boolean no_file = false, nouv_tent;
		int nb_tent = -1;
		int nb_tent_max = 2;
		do {
			nouv_tent = false;
			//no_file=false;
			nb_tent++;
			try {
				logger = Logger.getLogger("tinyeiffel.compiler");
				if (!no_file) {
					Handler liste[]=logger.getHandlers();
					trouve=false;
					if(liste!=null&&liste.length>0)
					{
						int i;
						for(i=0;i<liste.length;i++)
						{
							if(liste[i]==fh)
							{
								trouve=true;
								break;
							}
						}
					}
					//System.out.println("coucou");
					//assert(false);
					if(!trouve)
					{
						if (nb_tent < nb_tent_max - 1)
						{
							//fh = new FileHandler("compiler_eiffel_log.xml",);
							fh = new FileHandler("compiler_eiffel_log.xml",0,1);
						}
						else
						{
							assert(false);
							fh = new FileHandler();
						}
						logger.addHandler(fh);
					}
				}
				// Request that every detail gets logged.
				logger.setLevel(Level.ALL);
				// Log a simple INFO message.
				logger.info("Logger initialisé");
			} catch (SecurityException e) {
				e.printStackTrace();
				assert (false);
			} catch (IOException e) {
				long debut, fin;
				Handler liste[], h;
				e.printStackTrace();
				assert(false):e;
				assert (nb_tent < nb_tent_max);
				nouv_tent = true;
				no_file = true;
				liste = logger.getHandlers();
				if (liste != null) {
					for (int i = 0; i < liste.length; i++) {
						h = liste[i];
						assert (h != null);
						logger.removeHandler(h);
						h.close();
					}
				}
				debut = System.currentTimeMillis();
				do {
					fin = System.currentTimeMillis();
				} while (debut + 1 * 100 > fin);
			}
		} while (nouv_tent && nb_tent < nb_tent_max - 1);
		assert (nb_tent < nb_tent_max && !nouv_tent) : "nb_tent=" + nb_tent
				+ ";nouv_tent=" + nouv_tent;
	}

	protected void init_profile()
	{
		try{
			profiler=Logger.getLogger("tinyeiffel.profiler");
			fh2 = new FileHandler("compiler_eiffel_prof.xml",0,1);
			profiler.addHandler(fh2);
			//profiler.setLevel(Level.OFF);
		} catch(IOException e)
		{
			System.err.println("Exception:"+e);
			e.printStackTrace();
			assert(false):e;
		}
	}
	
	public void termine() {
		assert (initialise);
		termine_logger();
		termine_profile();
		initialise = false;
		assert (!initialise);
	}

	/**
	 * 
	 */
	protected void termine_logger() {
		if (fh != null) {
			logger.removeHandler(fh);
			fh.close();
		}
		logger = null;
		fh = null;
	}

	protected void termine_profile()
	{
		if (fh2 != null) {
			profiler.info("fin "+info_mem());
			profiler.removeHandler(fh2);
			fh2.close();
		}
		profiler = null;
		fh2 = null;
	}
	
	protected int stop;

	public static final int apres_context = 1, code_interm = 2, exec_prog = 3,
			genere_c = 4,convert=5,genere_java=6,interpretation2=7;

	public int compile(String nom_fichier,String chemin[]) {
		Type t;
		Classe c;

		try {
			//assert(false);
			profiler.info("Compilation de "+nom_fichier+" "+info_mem());
			verif_globale=new VerificationsGlobales(logging);
			System.out.println("Compilation de " + nom_fichier);
			logger.info("Compilation de " + nom_fichier);
			configure(nom_fichier,chemin);
			profiler.info("configuration lue "+info_mem());
			if(configuration==null)
			{
				logging.erreur(new ErreurSource("Erreur dans le fichier Ace",1,1,nom_fichier));
			}
			if (nb_erreur() > 0) {
				logging.affiche();
				termine();
				//if(c==null)
				return nb_erreur();
			}
			entier = new TypeSimple(false, "integer", new Vector());
			{
				Vector v = new Vector();
				v.add(new TypeSimple(false, "integer", new Vector()));
				tab_entier = new TypeSimple(false, "array", v);
			}
			bool = new TypeSimple(false, "boolean", new Vector());
			car = new TypeSimple(false, "character", new Vector());
			reel = new TypeSimple(false, "real", new Vector());
			dbl = new TypeSimple(false, "double", new Vector());
			str = new TypeSimple(false, "string", new Vector());
			verifications_classe=new Vector();
			// parsing de la classe racine
			profiler.info("Parsing de "+configuration.nom_classe.nom+" ...");
			c = parse_file(/* nom_fichier */configuration.nom_classe.nom + ".e");
			profiler.info("parsing fait "+info_mem());
			//System.out.println("Suite");
			if (nb_erreur() > 0) {
				logging.affiche();
				termine();
				//if(c==null)
				return nb_erreur();
			}
			liste_classe.addElement(c);
			ajoute_type(c.liste_classe);
			verifie_classe(c);
			// parsing de ANY
			profiler.info("Parsing de any ...");
			c = parse_file("any.e");
			profiler.info("parsing fait "+info_mem());
			if (c == null) {
				logging.erreur(new ErreurVHAY());
			}
			if (nb_erreur() > 0) {
				logging.affiche();
				termine();
				//if(c==null)
				return nb_erreur();
			}
			assert(c!=null);
			liste_classe.addElement(c);
			ajoute_type(c.liste_classe);
			verifie_classe(c);
			// creation de NONE
			c = new Classe(false, false, new TypeSimple(false, "NONE", null),
					new Vector(), null, null, null, null, null, null);
			liste_classe.addElement(c);
	
			// parsing de toutes les classes de l'univers
			int i;
			for (i = 0; i < liste_type.size(); i++) {
				t = (Type) liste_type.elementAt(i);
				if (t.nom.compareToIgnoreCase("any") != 0
						&& t.nom.compareToIgnoreCase("none") != 0) {
					boolean trouve = false;
					for (int j = 0; j < liste_classe.size() && !trouve; j++) {
						Classe c1 = (Classe) liste_classe.elementAt(j);
						assert (c1 != null);
						if (c1.nom.compareToIgnoreCase(t.nom) == 0)
							trouve = true;
					}
					if (!trouve) {
						profiler.info("Parsing de "+t.nom+" ...");
						c = parse_file(t.nom + ".e");
						profiler.info("parsing fait "+info_mem());
						System.out.println("fin du parsing");
						if (nb_erreur() > 0) {
							logging.affiche();
							//if(c==null)
							termine();
							return nb_erreur();
						}
						if (c != null) {
							liste_classe.addElement(c);
							ajoute_type(c.liste_classe);
							verifie_classe(c);
						}
					}
				}
			}
			profiler.info("Fin des parsing "+info_mem());
			// affichage de toutes les classes
			profiler.info("affichage des classes");
			System.out.println("Classes chargees:");
			logger.info("Classes chargees:");
			affiche_classe();
			if (nb_erreur() > 0) {
				logging.affiche();
				termine();
				//if(c==null)
				return nb_erreur();
			}
			profiler.info("Affichage réalisé "+info_mem());
			if(incremental)
			{// Vérifications globales
				profiler.info("Vérifications Globales...");
				boolean res=complete_verifications();
				profiler.info("Fin des vérifications");
				if(!res)
				{
					if(nb_erreur() > 0)
						logging.affiche();
					termine();
					System.out.println("Il y a des erreurs contextuelles.");
					return 1;
				}
				else
				{
					System.out.println("Il n'y a pas d'erreur.");
					if(1==0)
					{
						
					}
					else
					{
						return 0;
					}
				}
			}
			profiler.info("1ere annalyse contextuelle ...");
			context = new Context(logging, this);
			env = new Environnement(context);
			// 1ere verification contextuelle des classes :
			// heritage
			context.construit_classes(liste_classe, liste_type);
			table_heritage_directe = context.table_heritage_directe;
			table_heritage = context.table_heritage;
			profiler.info("1ere analyse réalisée "+info_mem());
			profiler.info("affichage des heritages");
			affiche_heritage_directe();
			affiche_heritage();
			profiler.info("Affichage fait "+info_mem());
	
			if (stop == apres_context) {
				termine();
				return nb_erreur();
			}
	
			// 2eme verification contextuelle des classes
			// contenu des features
			profiler.info("2eme annalyse contextuelle ...");
			int j, k;
			for (i = 0; i < liste_classe.size(); i++) {
				c = (Classe) liste_classe.elementAt(i);
				Feature f;
				//context.classe_courante = c;
				Table_Symbol table = context.donne_table_symbol(c);
				env.entre_classe(c, table);
				if (i == 0) {// La classe Racine
					if (c.type.generique != null && c.type.generique.length > 0) {
						context.erreur(new ErreurVSRC1(c));
					}
					//if()
				}
				if (c.creation != null && c.creation.length > 0) {
					Creation c1, c2;
					NomFeature nom1, nom2;
					Vector liste = new Vector();
					if (c.deferred) {// Erreur VGCP1
						context.erreur(new ErreurVGCP1(c));
					}
					for (j = 0; j < c.creation.length; j++) {
						c1 = c.creation[j];
						if (c1.nom_fonction != null) {
							for (k = 0; k < c1.nom_fonction.length; k++) {
								nom1 = c1.nom_fonction[k];
								if (liste.contains(nom1)) {// erreur VGCP2
									int no = liste.indexOf(nom1);
									context.erreur(new ErreurVGCP2(c, nom1,
											(NomFeature) liste.elementAt(no)));
								} else {
									Attribut_Complet ac;
									ac = table.donne_attribut(nom1);
									if (ac == null) {// Erreur VGCP3
										context.erreur(new ErreurVGCP3(c, nom1));
									} else {
										Feature ft = ac.getFeature();
										if (ft instanceof FeatureRoutine
												&& ((FeatureRoutine) ft).once) {
											// Erreur VGCP4
											context.erreur(new ErreurVGCP4(c, nom1,
													ac.getFeature()));
										}
										//Vector v=new Vector();
										//v.add(new Type(false,"integer",new
										// Vector()));
										//Type t1=new Type(false,"array",v);
										if (i == 0
												&& (ft.param != null && (ft.param.length > 1 || (ft.param.length == 1 && !ft.param[0].type
														.egaux(tab_entier))))) {// Erreur
																				// VSRC2
											context.erreur(new ErreurVSRC2(c, nom1,
													ft));
										}
									}
									liste.addElement(nom1);
								}
							}
						}
					}
					if (c.expanded) {
						if (liste.size() > 1) {// Erreur VGCP5
							context.erreur(new ErreurVGCP5(c));
						} else {
							Attribut_Complet ac = table
									.donne_attribut(c.creation[0].nom_fonction[0]);
							Feature ft = ac.getFeature();
							if (ft.param != null && ft.param.length > 0) {// Erreur
																		  // VGCP5
								context.erreur(new ErreurVGCP5(c,
										c.creation[0].nom_fonction[0], ft));
							}
						}
					}
				}
				if (c.feature != null) {
					for (j = 0; j < c.feature.length; j++) {
						f = c.feature[j];
						//context.feature_courante = f;
						env.entre_feature(f);
						verifie_feature(context);
						env.sort_feature();
					}
				}
				env.entre_invariant();
				verifie_liste_assertion(c, null, c.invariant, dans_invariant);
				env.sort_invariant();
				env.sort_classe();
			}
			profiler.info("2eme analyse réalisée "+info_mem());
			profiler.info("Affichage de la table des symbols");
			context.affiche_table_symbol();
			profiler.info("Affichage fait "+info_mem());
	
			System.out.println("Compilation du fichier " + nom_fichier);
			logger.info("Compilation du fichier " + nom_fichier);
			if (nb_erreur() > 0) {
				logging.affiche();
			} else {
				//logging.affiche();
				if (stop == code_interm||stop==genere_java
						||stop==interpretation2) {// generation du code intermediaire
					tinyeiffel.intermediaire.CIProgramme p;
					Convertie_Interm conv;
					CIInputXML inp;
					CIProgramme routines_internes;
					File fichier_extern;
					// récupération des routines fondamentales
					//inp=new CIInputXML("intern/kernel.xml");
					fichier_extern=new File(configuration.extern_kernel());
					assert(fichier_extern.exists()):
						"Impossible de trouver le fichier extern "+
						configuration.extern_kernel();
					inp=new CIInputXML(fichier_extern.getAbsolutePath());
					routines_internes=inp.programme;
					profiler.info("conversion code intermediaire..."+info_mem());
					conv = new Convertie_Interm(this,routines_internes);
					p = conv.convertie();
					profiler.info("Conversion faite "+info_mem());
					conv=null;
					System.gc();
					profiler.info("Liberation de l'objet conversion fait "+info_mem());
					System.out.println("coucou");
					System.out.println("Résultat:");
					logger.info("Résultat:");
					//p.affiche(System.out);
					try {
						profiler.info("generation du fichier de code intermediaire...");
						FileOutputStream out;
						out = new FileOutputStream("resultat.txt");
						PrintStream stream = new PrintStream(out);
						p.affiche(stream);
						stream.close();
						String nom_sortie = get_nom_sortie();
						out = new FileOutputStream(nom_sortie);
						stream = new PrintStream(out);
						p.toXML(stream);
						stream.close();
						profiler.info("Génération faite "+info_mem());
						//System.gc();
						programme = p;
						if(true)
						{
							CIVerification v;
							v=new CIVerification(programme);
							if(v.verification())
							{
								logger.info("Code intermediaire Ok");
								System.out.println("Code intermediaire Ok");
							}
							else
							{
								logger.info("Erreur dans le code intermediaire !!!");
								System.out.println("Erreur dans le code intermediaire !!!");
								context.erreur(new ErreurIntermediaire(
										"Erreur dans le code intermediaire:"+
										v.message_erreur2()));
								logging.affiche();
							}
						}
						if(false)
						{// test du code généré
							// Attention : OutOfMemory avec GenereClassTest
							File f;
							f=new File(nom_sortie);
							assert(f.exists());
							assert(f.length()>0);
							// liberation de la memoire
							profiler.info("Vidage memoire avant tests... "+info_mem());
							stream=null;
							out=null;
							liste_classe=null;
							context=null;
							this.env=null;
							this.liste_type=null;
							this.table_heritage=null;
							this.table_heritage_directe=null;
							conv=null;
							//p=null;  // le programme sous forme intermediaire
							System.gc();
							profiler.info("Vidage memoire avant tests fait "+info_mem());
							// test
							System.gc();
							profiler.info("test du fichier généré...");
							//tinyeiffel.intermediaire.CIInputXML inp;
							profiler.info("Parsing XML de "+nom_sortie+" ... "+info_mem());
							inp = new /*tinyeiffel.intermediaire.*/CIInputXML(nom_sortie);
							profiler.info("Fin du parsing "+info_mem());
							System.gc();
							profiler.info("Analyse 1 ... "+info_mem());
							out = new FileOutputStream("salut.xml");
							stream = new PrintStream(out);
							inp.programme.toXML(stream);
							stream.close();
							System.out.println("Analyse 1 ...");
							profiler.info("Analyse 1 ... "+info_mem());
							p.check_egal(p);
							System.out.println("Analyse 2 ...");
							profiler.info("Analyse 2 ... "+info_mem());
							inp.programme.check_egal(inp.programme);
							System.out.println("Analyse 3 ...");
							profiler.info("Analyse 3 ... "+info_mem());
							inp.programme.check_egal(p);
							System.out.println("Analyse 4 ...");
							profiler.info("Analyse 4 ... "+info_mem());
							p.check_egal(inp.programme);
							if (stop >= exec_prog) {
								System.out.println("Execution du programme");
								assert (false);
								p.run(null, null);
							}
							profiler.info("tests faits "+info_mem());
						}
						if(false)
						{// vidage de la memoire
							Table_Symbol ts[],ts2;
							Attribut_Complet ac;
							Attribut_Heritage ah;
							profiler.info("Memoire avant GC : "+info_mem());
							stream=null;
							out=null;
							liste_classe=null;
							ts=context.table_symbol;
							for(i=0;i<ts.length;i++)
							{
								ts2=ts[i];
								ts2.classe=null;
								ts2.context=null;
								ts2.log=null;
								ts2.heritage=null;
								if(ts2.liste_attribut!=null)
								{
									for(j=0;j<ts2.liste_attribut.length;j++)
									{
										ac=ts2.liste_attribut[j];
										if(ac.feature_directe!=null)
										{
											ac.feature_directe.classe=null;
											if(ac.feature_directe.type_retour!=null)
											{
												//assert(ac.feature_directe.type_retour.classe_env==null);
											}
											ac.feature_directe=null;
										}
										if(ac.feature_reel!=null)
										{
											ac.feature_reel.classe=null;
											if(ac.feature_reel.type_retour!=null)
											{
												//assert(ac.feature_reel.type_retour.classe_env==null);
											}
											ac.feature_reel=null;
										}
										if(ac.attribut_directe!=null)
										{
											ac.attribut_directe.classe=null;
											ac.attribut_directe=null;
										}
										if(ac.attribut_ancetre!=null)
										{
											for(k=0;k<ac.attribut_ancetre.length;k++)
											{
												ah=ac.attribut_ancetre[k];
												ah.classe=null;
											}
										}
										ac.classe=null;
									}
								}
							}
							context=null;
							this.env=null;
							this.liste_type=null;
							this.table_heritage=null;
							this.table_heritage_directe=null;
							conv=null;
							p=null;  // le programme sous forme intermediaire
							System.gc();
							profiler.info("Memoire apres GC : "+info_mem());
						}
						if(stop == genere_java)
						{
							if(nb_erreur() > 0)
							{// il y a des erreurs
								
							}
							else
							{
								assert(p!=null);
								logger.info("Generation code java...");
								ConvertieJava conv_java;
								conv_java=new ConvertieJava(p,logger,logging);
								conv_java.generation();
								/*ConvertieJava2 conv_java;
								conv_java=new ConvertieJava2(p,logger);
								conv_java.generation();*/
							}
						}
						else if(stop==interpretation2)
						{
							if(nb_erreur() > 0)
							{// il y a des erreurs
								
							}
							else
							{
								assert(p!=null);
								logger.info("Interpretation du code intermediaire...");
								Interpreteur2 interp;
								interp=new Interpreteur2();
								interp.execution(p,logger,null);
							}
						}
					} catch (SecurityException e) {
						e.printStackTrace();
						assert (false);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						assert (false);
					}
					
					//test_gc(true);
				} else if (stop == exec_prog) {// interpretation
					Interpreteur inter;
					inter = new Interpreteur(this);
				} else if (stop == genere_c) {
					GenC genc;
					genc = new GenC(this);
				} else if (stop == convert) {
					Convertion conv;
					conv = new Convertion(this);
				}
			}
			System.out.println("Fin de la compilation");
			logger.info("Fin de la compilation");
			//test_gc(true);
			test_gc(false);
		} finally {
			if(initialise)
			{
				profiler.info("Fin de la compilation "+info_mem());
				termine();
			}
			else
			{
				//assert(false);
			}
		}
		return nb_erreur();
	}

	public void verifie_classe(Classe c)
	{
		Verifications v;
		//Convertie_Interm conv;
		CIClasse cl;
		Attribut_Complet ac;
		String chemin_fichier;
		assert(c!=null);
		if(!incremental)
			return;
		profiler.info("verification de "+c.nom+" ..."+info_mem());
		
		v=new Verifications(c.type,c.heritage,logging);
		//conv=new Convertie_Interm(this);
		cl=new CIClasse();
		if(c.feature!=null)
		{
			Feature f;
			NomFeature nf;
			int i,j;
			boolean est_once;
			CIRoutine routine;
			CIAttribut attr;
			tinyeiffel.ast.Attribut attr2;
			for(i=0;i<c.feature.length;i++)
			{
				f=c.feature[i];
				if(f instanceof FeatureRoutine&&
						((FeatureRoutine)f).once)
				{
					est_once=true;
				}
				else
				{
					est_once=false;
				}
				/*attr2=new tinyeiffel.ast.Attribut(f.liste_nom[0],c,f);
				ac=new Attribut_Complet(f.liste_nom[0],null);
				ac.attribut_directe=attr2;
				ac.no_attribut_reel=-1;
				ac.feature_directe=f;
				ac.verifie=true;
				attr=conv.convertie_attribut(ac,false,cl,new Vector());*/
				profiler.info("ajout attribut local "+f.liste_nom[0]+"..."+info_mem());
				v.ajoute_attribut(f);
				profiler.info("ajout attribut local fait"+info_mem());
				/*for(j=0;j<f.liste_nom.length;j++)
				{
					nf=f.liste_nom[j];
					v.ajoute_attribut(nf,f.param,f.type_retour,est_once);
				}*/
			}
		}
		chemin_fichier=donne_nom(c.nom+".e");
		assert(chemin_fichier!=null);
		assert(chemin_fichier.length()>0);
		v.genere_xml(chemin_fichier);
		//verifications_classe.addElement(v);
		profiler.info("ajout global ... "+info_mem());
		verif_globale.ajoute(v);
		profiler.info("ajout global fait "+info_mem());
		profiler.info("verif fait "+info_mem());
	}
	
	public boolean complete_verifications()
	{
		//VerificationsGlobales vg;
		//vg=new VerificationsGlobales(verifications_classe);
		//return vg.traitement();
		return verif_globale.traitement()&&nb_erreur()==0;
	}
	
	public String get_nom_sortie() {
		String res = "", rep;
		NomGrappe nom_grappe;
		Grappe grappe;
		int i;
		assert (configuration != null);
		grappe = configuration.donne_grappe_racine();
		assert (grappe != null);
		res += grappe.donne_repertoire();
		res += "resultat.xml";
		return res;
	}

	public static String info_mem()
	{
		Runtime r;
		long free, total, max,used;
		r = Runtime.getRuntime();
		free = r.freeMemory();
		total = r.totalMemory();
		max = r.maxMemory();
		used=total-free;
		return "(mem utilisée="+affiche_mem(used)+
			" ; total="+affiche_mem(total)+")";
		//return "(free mem=" + free + " ; total mem=" + total + " ; max mem="
		//		+ max+")";
	}
	
	public static String affiche_mem(long m)
	{
		long kilo=1024;
		if(m>kilo)
		{
			if(m>kilo*kilo)
			{
				return (m/(kilo*kilo))+" Mo";
			}
			else
			{
				return (m/kilo)+" Ko";
			}
		}
		else
		{
			return m+" octets";
		}
	}
	
	public void test_gc(boolean vidage) {
		long fm1, fm2, tm1, tm2, mm1, mm2;
		long dfm, dtm, dmm;
		long d1, d2, dd;
		Runtime r;
		d1 = System.currentTimeMillis();
		r = Runtime.getRuntime();
		fm1 = r.freeMemory();
		tm1 = r.totalMemory();
		mm1 = r.maxMemory();
		logger.info("free mem=" + fm1 + " ; total mem=" + tm1 + " ; max mem="
				+ mm1);
		if (vidage) {
			this.context = null;
			this.env = null;
			this.configuration = null;
			this.liste_classe = null;
			//this.logger=null;
			this.logging = null;
			this.table_heritage = null;
			this.table_heritage_directe = null;
		}
		logger.info("GC ...");
		r.gc();
		fm2 = r.freeMemory();
		tm2 = r.totalMemory();
		mm2 = r.maxMemory();
		dfm = fm2 - fm1;
		dtm = tm2 - tm1;
		dmm = mm2 - mm1;
		logger.info("free mem=" + fm2 + " ; total mem=" + tm2 + " ; max mem="
				+ mm2);
		logger.info("diff free mem=" + dfm + " ; diff total mem=" + dtm
				+ " ; diff max mem=" + dmm);
		d2 = System.currentTimeMillis();
		dd = d2 - d1;
		logger.info("duree total : " + dd + " ms = " + (dd / 1000) + " s");
	}

	public Environnement env;

	public void affiche_erreur() {
		logging.affiche();
	}

	public int nb_erreur() {
		return logging.nb_erreur();
	}

	public void erreur(String t) {
		logging.erreurMsg(t);
	}

	public void erreur(Erreur e) {
		assert (e != null);
		logging.erreur(e);
	}

	public void erreur(String t, Position p) {
		if (p == null) {
			System.out.println("t=" + t);
			assert (p != null);
		}
		logging.erreurMsg(t + p);
	}

	public void warning(String t) {
		logging.warningMsg(t);
	}

	public void warning(String t, Position p) {
		logging.warningMsg(t + p);
	}

	public void info(String t) {
		logging.infoMsg(t);
	}

	public void info(String t, Position p) {
		logging.infoMsg(t + p);
	}

	public void fatal(String t) {
		logging.fatalMsg(t);
	}

	public void fatal(String t, Position p) {
		logging.fatalMsg(t + p);
	}

	public void verifie_feature(Context context) {
		int i, j;
		Instr instr;
		Classe c = env.classe;//context.classe_courante;
		Feature f = env.feature;//context.feature_courante;
		Table_Symbol table = context.donne_table_symbol(c);
		DeclareVar param[] = f.param, var1, var2;
		Attribut_Complet liste_attr[], ac;

		if (param != null) {
			for (i = 0; i < param.length; i++) {
				var1 = param[i];
				assert (var1 != null);
				for (j = 0; j < i; j++) {
					var2 = param[j];
					assert (var1 != var2);
					assert (var2 != null);
					if (var1.nom.compareToIgnoreCase(var2.nom) == 0) {// Erreur
																	  // VREG
						context.erreur(new ErreurVREG(c, f, var2, var1, true));
					}
				}
				ac = table.trouve_attribut(var1.nom);
				if (ac != null) {// Erreur VRFA
					context.erreur(new ErreurVRFA(c, f, ac.nom, var1));
				}
			}
		}
		if (f instanceof FeatureRoutine) {
			FeatureRoutine f1 = (FeatureRoutine) f;
			DeclareVar local[] = f1.local;

			System.out.println("feature0:" + f1.liste_nom[0]);
			if (f.require != null) {
				env.entre_require();
				verifie_liste_assertion(c, f, f.require, dans_require);
				env.sort_require();
			}
			if (local != null) {
				for (i = 0; i < local.length; i++) {
					var1 = local[i];
					assert (var1 != null);
					for (j = 0; j < i; j++) {
						var2 = local[j];
						assert (var1 != var2);
						assert (var2 != null);
						if (var1.nom.compareToIgnoreCase(var2.nom) == 0) {// Erreur
																		  // VREG
							context.erreur(new ErreurVREG(c, f, var2, var1,
									false));
						}
					}
					if (param != null) {
						for (j = 0; j < param.length; j++) {
							var2 = param[j];
							assert (var1 != var2);
							assert (var2 != null);
							if (var1.nom.compareToIgnoreCase(var2.nom) == 0) {// Erreur
																			  // VREG2
								context
										.erreur(new ErreurVRLE2(c, f, var2,
												var1));
							}
						}
					}
					ac = table.trouve_attribut(var1.nom);
					if (ac != null) {// Erreur VRLE1
						context.erreur(new ErreurVRLE1(c, f, ac.nom, var1));
					}
					context.type_valide(table, var1.type);
				}
			}
			env.entre_do();
			verifie_liste_instr(context, f1.liste_instr);
			env.sort_do();
			if (f1.rescue != null) {
				env.entre_rescue();
				context.dans_rescue = true;
				verifie_liste_instr(context, f1.rescue);
				context.dans_rescue = false;
				env.sort_rescue();
			}
			if (f.ensure != null) {
				env.entre_ensure();
				verifie_liste_assertion(c, f, f.ensure, dans_ensure);
				env.sort_ensure();
			}
			/*
			 * if(f1.liste_instr!=null) { /*for(i=0;i
			 * <f1.liste_instr.length;i++) { instr=f1.liste_instr[i];
			 * verifie_instr(c,f,instr); }
			 */
			//}t=verifie_expr(c,f,ins.expr);
		} else if (f instanceof FeatureUnique) {
			FeatureUnique f1 = (FeatureUnique) f;
			if (f1 != null) {
				if (f1.type_retour == null
						|| !f1.type_retour.egaux(new TypeSimple(false, "integer",
								new Vector()))) {// Erreur VQUI
					erreur(
					//"une feature unique doit avoir un type"
					//	+ " de retour integer",
					//f1.debut()
					new ErreurVQUI(c, f1));
				}
			}
		} else if (f instanceof FeatureExternal) {
			if (f.require != null) {
				env.entre_require();
				verifie_liste_assertion(c, f, f.require, dans_require);
				env.sort_require();
			}
			FeatureExternal f1 = (FeatureExternal) f;
			if (f.ensure != null) {
				env.entre_ensure();
				verifie_liste_assertion(c, f, f.ensure, dans_ensure);
				env.sort_ensure();
			}
		} else if (f instanceof FeatureDeferred) {
			if (f.require != null) {
				env.entre_require();
				verifie_liste_assertion(c, f, f.require, dans_require);
				env.sort_require();
			}
			FeatureDeferred f1 = (FeatureDeferred) f;
			if (f.ensure != null) {
				env.entre_ensure();
				verifie_liste_assertion(c, f, f.ensure, dans_ensure);
				env.sort_ensure();
			}
		} else if (f instanceof FeatureAttr) {
			FeatureAttr f1 = (FeatureAttr) f;
		} else if (f instanceof FeatureExpr) {
			FeatureExpr f1 = (FeatureExpr) f;
			Type t;
			Expr expr = f1.expr;
			if (expr.op == Expr.MoinsU || expr.op == Expr.PlusU) {
				Expr e = ((Expr_Unaire) f1.expr).expr1;
				if (e != null) {
					if (e.op == Expr.Entier || e.op == Expr.Reel)
						expr = e;
				}
			}
			switch (expr.op) {
			case Expr.Chaine:
				if (f1.type_retour == null
						|| !f1.type_retour.egaux(new TypeSimple(false, "string",
								new Vector()))) {// Erreur VQMC5
					context.erreur(new ErreurVQMC5(c, f1));
				}
				break;
			case Expr.Char:
				if (f1.type_retour == null
						|| !f1.type_retour.egaux(new TypeSimple(false, "character",
								new Vector()))) {// Erreur VQMC2
					context.erreur(new ErreurVQMC2(c, f1));
				}
				break;
			case Expr.Entier:
				if (f1.type_retour == null
						|| !f1.type_retour.egaux(new TypeSimple(false, "integer",
								new Vector()))) {// Erreur VQMC3
					context.erreur(new ErreurVQMC3(c, f1));
				}
				break;
			case Expr.Reel:
				if (f1.type_retour == null
						|| !(f1.type_retour.egaux(new TypeSimple(false, "real",
								new Vector())) || f1.type_retour
								.egaux(new TypeSimple(false, "double", new Vector())))) {// Erreur
																				   // VQMC4
					context.erreur(new ErreurVQMC4(c, f1));
				}
				break;
			case Expr.Vrai:
			case Expr.Faux:
				if (f1.type_retour == null
						|| !f1.type_retour.egaux(new TypeSimple(false, "boolean",
								new Vector()))) {// Erreur VQMC1
					context.erreur(new ErreurVQMC1(c, f1));
				}
				break;
			//case Expr.Var:
			//	break;
			default://	Erreur VQMC5
				//assert(false);
				context.erreur(new ErreurVQMC(c, f1));
				break;
			}
			t = verifie_expr(context, f1.expr, true);
			/*
			 * if (!context.type_compatible(t,table, f1.type_retour,table)) {
			 * erreur( "la constante n'est pas du type des attributs",
			 * f.debut()); }
			 */
		} else {
			erreur("feature inconnue", f.debut());
		}
	}

	public void verifie_liste_instr(Context context, Instr liste_instr[]) {
		Instr instr;
		int i;
		Classe c = env.classe;//context.classe_courante;
		Feature f = env.feature;//context.feature_courante;

		if (liste_instr == null) {
			return;
		}
		for (i = 0; i < liste_instr.length; i++) {
			instr = liste_instr[i];
			verifie_instr(context, instr);
		}
	}

	protected static final int dans_invariant = 1;

	protected static final int dans_require = 2;

	protected static final int dans_ensure = 3;

	protected static final int dans_invariant_boucle = 4;

	protected static final int dans_check = 5;

	//protected static final int dans_require=5;

	public void verifie_instr(Context context, Instr instr) {
		Type t;
		Classe c = env.classe;//context.classe_courante;
		Feature f = env.feature;//context.feature_courante;
		Table_Symbol table = context.donne_table_symbol(c);

		if (instr instanceof Instr_If) {
			Instr_If ins = (Instr_If) instr;
			Instr ins2;
			Instr_ElseIf ins3;
			Instr_Else ins4;

			t = verifie_expr(context, ins.expr, true);
			if (t == null) {
				erreur("Erreur1:expression sans type" + ins.expr.op, ins.expr
						.debut());
				return;
			}
			if (!t.egaux(bool)) //(t.nom.compareToIgnoreCase("boolean") != 0)
			{
				erreur(new ErreurVWBE(c, ins.expr, ErreurVWBE.type_if));
				//"Erreur:Le test dans le if doit etre un booleen",
				//ins.debut());
			}
			verifie_liste_instr(context, ins.liste_instr);
			ins2 = ins.getSuivant();
			while (ins2 instanceof Instr_ElseIf) {
				ins3 = (Instr_ElseIf) ins2;
				t = verifie_expr(context, ins3.expr, true);
				if (t == null) {
					erreur("Erreur2:expression sans type", ins3.expr.debut());
					return;
				}
				if (!t.egaux(bool)) {
					erreur(new ErreurVWBE(c, ins3.expr, ErreurVWBE.type_elseif));
				}
				/*
				 * if (t.nom.compareToIgnoreCase("boolean") != 0) { erreur(
				 * "Erreur:Le test dans le elseif doit etre un booleen",
				 * ins3.expr.debut()); }
				 */
				verifie_liste_instr(context, ins3.liste_instr);
				ins2 = ins3.getSuivant();
			}
			if (ins2 instanceof Instr_Else) {
				ins4 = (Instr_Else) ins2;
				verifie_liste_instr(context, ins4.liste_instr);
			}
		} else if (instr instanceof Instr_Affect) {
			Instr_Affect ins = (Instr_Affect) instr;
			Type t1, t2, t3, t4 = null;

			if (!context.var_existe(ins.nom)) {
				logging.erreur(new ErreurVEEN(c, ins.nom, ins.tid));
			} else if (!context.variable_assignable(ins.nom, ins.tid.debut())) {
				logging.erreur(new ErreurAffect(c, ins, ins.nom));
			} else {
				t1 = context.donne_type_var(ins.nom, ins.tid.debut());
				if (t1 != null) {
					Table_Symbol table2;
					//t3=table.trouve_vrai_type(t1);
					//assert(!t3.is_like);
					t2 = verifie_expr(context, ins.expr, true);
					t2 = ins.expr.type;
					//assert(t2!=null):"expr="+ins.expr+"=:"+ins.nom;
					//if(t2!=null)
					//	t4=table.trouve_vrai_type(t2);
					Declare_entite entite = context.donne_entite(ins.nom,
							ins.tid.debut());
					/*
					 * if(ins.expr.classe!=null) {
					 * table2=context.donne_table_symbol(ins.expr.classe); }
					 * else
					 */
					{
						table2 = table;
					}
					if (!ins.expr.erreur
							&& t1 != null
							&& t2 != null
							&& entite != null
							&& !context.type_compatible(ins.expr, table,
									entite, table2)) {
						//erreur("Type incompatible dans l'affectation",
						// instr.debut());
						/*
						 * assert(!t4.is_like); assert(ins.expr instanceof
						 * Expr_Unaire); assert(((Expr_Unaire)ins.expr).expr1
						 * instanceof Expr_Entier);
						 * assert(verifie_expr(context,((Expr_Unaire)ins.expr).expr1).nom.compareToIgnoreCase("integer")==0);
						 * assert(t4.nom.compareToIgnoreCase("integer")==0):
						 * "t4="+t4+",t2="+t2+",expr="+
						 * ((Expr_Unaire)ins.expr).expr1+
						 * ",debut="+ins.expr.debut();
						 */
						//logging.erreur(new ErreurVBAR());
						logging.erreur(new ErreurVJAR(c, ins, t2, t1));
					}
				}
			}
		} else if (instr instanceof Instr_TentAffect) {
			Instr_TentAffect ins = (Instr_TentAffect) instr;
			Type t1, t2;

			if (!context.var_existe(ins.nom)) {
				logging.erreur(new ErreurVEEN(c, ins.nom, ins.tid));
			} else if (!context.variable_assignable(ins.nom, ins.tid.debut())) {
				logging.erreur(new ErreurAffect(c, ins, ins.nom));
			} else {
				t1 = context.donne_type_var(ins.nom, ins.tid.debut());
				t2 = verifie_expr(context, ins.expr, true);
				if (t1 != null && t2 != null) {
					if (context.type_expanded(t1, table)) {
						erreur(new ErreurVJRV(c, ins, t1, t2));
					} else
					//if(t1!=null&&t2!=null&&!type_compatible(t2,t1))
					{
						//	erreur("Type incompatible dans l'affectation");
					}
				}
			}
		} else if (instr instanceof Instr_Retry) {
			if (!env.dans_rescue())//(!context.dans_rescue)
			{// Erreur VXRT
				erreur(new ErreurVXRT(c, f, (Instr_Retry) instr));
			}
		} else if (instr instanceof Instr_Debug) {
			Instr_Debug ins = (Instr_Debug) instr;
			verifie_liste_instr(context, ins.instr);
		} else if (instr instanceof Instr_Loop) {
			Instr_Loop ins = (Instr_Loop) instr;
			t = verifie_expr(context, ins.expr, true);
			if (t != null && !t.egaux(bool) /*
											 * t.nom.compareToIgnoreCase("boolean") !=
											 * 0
											 */) {
				erreur(new ErreurVWBE(c, ins.expr, ErreurVWBE.type_loop));
				//"le test dans le loop doit etre un booleen",
				//ins.expr.debut());
			}//&é"'(-è
			if (ins.variant != null) {
				t = verifie_expr(context, ins.variant, true);
				if (t != null && !t.egaux(entier)) {// t.nom.compareToIgnoreCase("integer")
													// != 0) {
					erreur(new ErreurVAVE(c, ins));
					//"l'invariant dans le loop doit etre un entier",
					//ins.variant.debut());
				}
			}
			verifie_liste_instr(context, ins.from);
			verifie_liste_instr(context, ins.loop);
			verifie_liste_assertion(c, f, ins.invariant, dans_invariant_boucle);
		} else if (instr instanceof Instr_Check) {
			Instr_Check ins = (Instr_Check) instr;
			verifie_liste_assertion(c, f, ins.liste_expr, dans_check);
		} else if (instr instanceof Instr_Inspect) {
			Instr_Inspect ins = (Instr_Inspect) instr;
			t = verifie_expr(context, ins.expr, true);
			assert (t != null);
			if (!t.egaux(this.entier) && !t.egaux(this.car)) {
				erreur(new ErreurVOMB1(c, ins, t));
				//"l'expression dans l'inspect n'etre pas un type admis",
				//ins.expr.debut());
			} else {
				Expr e;
				Vector v;
				Type t2;
				Ensemble ens;
				boolean type_car = false;
				if (t.egaux(this.car)) {
					ens = new EnsembleCar(context, c, ins);
					type_car = true;
				} else {
					ens = new EnsembleInt(context, c, ins);
					type_car = false;
				}
				while (ins != null) {
					int i;
					for (i = 0; i < ins.when.size(); i++) {
						v = (Vector) ins.when.elementAt(i);
						if (v.size() == 1) {
							e = (Expr) v.elementAt(0);
							t2 = verifie_expr(context, e, true);
							if (!context.est_constant(table, e)) {
								erreur(new ErreurVOMB2(c, e));
							} else if (!context.type_compatible(t2, table, t,
									table)) {
								erreur(new ErreurVOMB2(c, e, t.egaux(entier)));
								//"type incompatible pour la constante",
								//e.debut());
							} else {
								if (type_car) {
									ElementCar elt;
									char c1 = donne_valeur_char(e, table);
									elt = new ElementCar(e, c1);
									//if(!ens.contient(elt))
									ens.ajoute(elt);
									//else
									{
										//context.erreur(new
										// ErreurVOMB3(c,ins,elt,ens.elt));
									}
								} else {
									ElementInt elt;
									//int c1=donne_valeur_int(e,table);
									//elt=new ElementInt(e,c1);
									elt = donne_valeur_int(e, table);
									//if(!ens.contient(elt))
									ens.ajoute(elt);
								}
							}
						} else if (v.size() == 2) {
							int nb_ok = 0;
							Expr e1, e2;
							e1 = e = (Expr) v.elementAt(0);
							t2 = verifie_expr(context, e, true);
							if (!context.est_constant(table, e)) {
								erreur(new ErreurVOMB2(c, e));
							} else if (!context.type_compatible(t2, table, t,
									table)) {
								erreur(new ErreurVOMB2(c, e, t.egaux(entier)));
								//"type incompatible pour la constante",
								//e.debut());
							} else
								nb_ok++;
							e2 = e = (Expr) v.elementAt(1);
							t2 = verifie_expr(context, e, true);
							if (!context.est_constant(table, e)) {
								erreur(new ErreurVOMB2(c, e));
							} else if (!context.type_compatible(t2, table, t,
									table)) {
								erreur(new ErreurVOMB2(c, e, t.egaux(entier)));
								//"type incompatible pour la constante",
								//e.debut());
							} else
								nb_ok++;
							{
								if (nb_ok == 2) {
									if (type_car) {
										ElementCar elt, elt2;
										char c1 = donne_valeur_char(e1, table);
										char c2 = donne_valeur_char(e2, table);
										elt = new ElementCar(e1, c1);
										elt2 = new ElementCar(e2, c2);
										ens.ajoute(elt, elt2);
									} else {
										ElementInt elt, elt2;
										elt = donne_valeur_int(e1, table);
										elt2 = donne_valeur_int(e2, table);
										ens.ajoute(elt, elt2);
									}
								}
							}
						} else {
							erreur("plus de 2 suites de constante", ins.debut());
						}
					}
					verifie_liste_instr(context, ins.liste_instr);
					ins = (Instr_Inspect) ins.getSuivant();
				}
			}
		} else if (instr instanceof Instr_Appel) {
			Instr_Appel ins = (Instr_Appel) instr;
			int i;
			boolean erreur = false;
			if (ins.nom != null) {
				Vector v = new Vector();
				if (ins.parametre != null) {
					for (i = 0; i < ins.parametre.length; i++) {
						v.addElement(ins.parametre[i]);
					}
				}
				t = verifie_appel(context, new NomFeature(ins.nom), v, null,
						ins.debut(), true);
			} else {
				t = verifie_expr(context, ins.expr, true);
			}
			//assert(t!=null);
			if (t == null && ins.getSuivant() != null) {// Erreur VKCN2
				erreur(new ErreurVKCN2(c, ins));
			} else {
				ins = (Instr_Appel) ins.getSuivant();
				while (ins != null) {
					//assert(!ins.nom.equalsIgnoreCase("tata2"));
					assert (t != null);
					Classe cl = context.cherche_classe(t);
					System.out.println("debut1:" + ins.nom + "type=" + t);
					if (cl != null) {
						//context.classe_courante=cl;
						System.out.println("debut2");
						Attribut_Complet a1 = context.donne_attribut(
								new NomFeature(ins.nom), cl);
						if (a1 == null) {
							erreur(new ErreurVUEX2(cl, new NomFeature(ins.nom),
									ins.debut())
							/*
							 * "la fonction " + ins.nom + " n'existe pas dans la
							 * classe " + cl.nom, ins.debut()
							 */);
							erreur = true;
						} else {
							System.out.println("debut3");
							Vector v = new Vector();
							if (ins.parametre != null) {
								for (i = 0; i < ins.parametre.length; i++) {
									v.addElement(ins.parametre[i]);
								}
							}
							t = verifie_appel(context, new NomFeature(ins.nom),
									v, cl, ins.debut(), false);
							System.out.println("debut4");
						}
						//context.classe_courante=c;
					} else {
						erreur = true;
						break;
					}
					//assert(!ins.nom.equalsIgnoreCase("tata2"));
					ins = (Instr_Appel) ins.getSuivant();
				}
				if (!erreur && t != null) {// Erreur VKCN2
					//a1 = context.donne_attribut(nom, c);
					Instr_Appel ins2 = (Instr_Appel) instr;
					while (ins2.getSuivant() != null)
						ins2 = (Instr_Appel) ins2.getSuivant();
					erreur(new ErreurVKCN2(c, ins2));
				}
			}
		} else if (instr instanceof Instr_Creation) {
			Instr_Creation ins = (Instr_Creation) instr;
			Vector v = new Vector();
			int i;
			String n;
			Type type_creation = null;
			Classe cl, cl_creation;
			/*
			 * if(ins.type!=null) cl=context.cherche_classe(ins.type); else {
			 */
			if (!context.var_existe(ins.nom)) {
				logging.erreur(new ErreurVEEN(c, ins.nom, ins.tid));
			} else if (!context.variable_assignable(ins.nom, ins.tid.debut())) {
				logging.erreur(new ErreurAffect(c, ins, ins.nom));
			} else {
				t = context.donne_type_var(ins.nom, ins.tid.debut());
				if (t == null) {
					//erreur();
					//erreur("attribut " + ins.nom + " inconnue", ins.debut());
					return;
				}
				type_creation = t;
				cl = context.cherche_classe(t);
				cl_creation = cl;
				//assert(cl!=null);
				//}
				if (ins.type != null) {
					Classe cl0 = context.cherche_classe(ins.type);
					if (!context.type_compatible(ins.type, table, t, table)) {
						//erreur(
						//	"le type de creation est incompatible avec la
						// variable",
						//	ins.debut());
						erreur(new ErreurVGCC3(c, ins, ins.type, t));
					}
					type_creation = ins.type;
					cl_creation = cl0;
				}
				assert (type_creation != null);
				if (table.type_generique(type_creation)) {// Erreur VGCC1
					erreur(new ErreurVGCC1(c, ins, type_creation));
				} else if (cl_creation.deferred) {// Erreur VGCC2
					erreur(new ErreurVGCC2(c, ins, type_creation));
				} else {
					if (ins.parametre != null) {
						for (i = 0; i < ins.parametre.length; i++) {
							v.addElement(ins.parametre[i]);
						}
					}
					if (cl_creation.creation == null
							|| cl_creation.creation.length == 0) {
						if (ins.nom2 != null) {
							erreur(new ErreurVGCC4(c, ins, type_creation));
						}
					} else {
						if (ins.nom2 == null) {
							erreur(new ErreurVGCC5(c, ins, type_creation,
									ErreurVGCC5.pas_appel));
						} else {
							Creation cr;
							NomFeature n1, n2;
							boolean trouve = false;
							n1 = new NomFeature(ins.nom2);
							for (int j = 0; !trouve
									&& j < cl_creation.creation.length; j++) {
								cr = cl_creation.creation[j];
								assert (cr != null);
								if (cr.nom_fonction != null) {
									for (int k = 0; !trouve
											&& k < cr.nom_fonction.length; k++) {
										n2 = cr.nom_fonction[k];
										assert (n2 != null);
										if (n2.equals(n1)) {
											trouve = true;
											if (cr.liste_type != null
													&& cr.liste_type.length > 0) {// recherche
																				  // si
																				  // incompatibilite
																				  // d'exportation
												boolean t0 = false;
												Type t1 = null;
												Table_Symbol table2;
												for (int m = 0; m < cr.liste_type.length; m++) {
													t1 = cr.liste_type[m];
													if (t1 != null) {
														table2 = context
																.donne_table_symbol(t1);
														if (t1 != null
																&& table2 != null
																&& context
																		.type_compatible(
																				c.type,
																				table,
																				t1,
																				table2)) {// trouver
																						  // un
																						  // type
																						  // compatible
															t0 = true;
															break;
														}
													}
												}
												if (!t0) {// Aucun type n'est
														  // compatible
													//assert(t1!=null);
													erreur(new ErreurVGCC6(c,
															ins));
												}
											}
											break;
										}
									}
								}
							}
							if (!trouve) {
								erreur(new ErreurVGCC5(c, ins, type_creation,
										ErreurVGCC5.pas_fonction));
							}
						}
					}
					if (ins.nom2 != null && !ins.nom2.equalsIgnoreCase("")) {
						n = ins.nom2;
					} else {
						n = ins.nom;
						//context.classe_courante=cl;
					}
					if (!context.var_existe(ins.nom)) {// variable inexistante
						erreur(new ErreurVEEN(c, ins.nom, ins.tid));
					} else {
						Type t1 = context.donne_type_var(ins.nom, ins.tid
								.debut());
						if (t1 == null) {
							erreur("la variable " + ins.nom
									+ " n'est pas declarée", ins.debut());
						} else {
							if (ins.type != null
									&& !context.type_compatible(t1, table,
											ins.type, table)) {
								/*
								 * erreur( "type incompatible pour l'instruction
								 * de création:" + t1.nom, ins.debut());
								 */
								erreur(new ErreurVGCC3(c, ins, type_creation, t));
							} else if (context.type_expanded(type_creation,
									table)) {
								erreur(new ErreurVGCC3(c, ins, type_creation));
							} else {
								if (ins.nom2 != null
										&& !ins.nom2.equalsIgnoreCase("")) {
									System.out.println("classe0:" + c.nom);
									//verifie_appel(context, ins.nom2, v,
									// cl,ins.debut(),false);
									Attribut_Complet a1 = context
											.donne_attribut(new NomFeature(
													ins.nom2), cl);
									if (a1 != null) {
										Feature f1;
										verifie_a_valide(context,
												new NomFeature(ins.nom2), v,
												cl, ins.debut(), a1);
										f1 = a1.getFeature();
										if (f1.type_retour != null) {
											erreur(new ErreurVGCC6(c, ins, f1,
													ErreurVGCC6.retour));
										} else if (f1 instanceof FeatureRoutine
												&& ((FeatureRoutine) f1).once) {
											erreur(new ErreurVGCC6(c, ins, f1,
													ErreurVGCC6.once));
										}
									}
								}
							}
						}
					}
				}
			}
			//context.classe_courante=c;
		} else {
			erreur("Instruction inconnue", instr.debut());
		}
	}

	public static Properties prop = null;

	public char donne_valeur_char(Expr e, Table_Symbol table) {
		assert (e != null);
		assert (table != null);
		switch (e.op) {
		case Expr.Char:
			Expr_Car e2 = (Expr_Car) e;
			if (e2.car.length() == 3)
				return e2.car.charAt(1);
			else if (e2.car.matches("\'(.)?\'"))
				assert (false);
			break;
		case Expr.Var:
			Expr_Var e3 = (Expr_Var) e;
			Attribut_Complet ac = table.trouve_attribut(e3.nom);
			assert (ac != null);
			assert (ac.getFeature() instanceof FeatureExpr);
			Expr e4;
			e4 = ((FeatureExpr) ac.getFeature()).expr;
			assert (e4 != null);
			// TODO : eviter une recursivite infini
			//assert(e4 instanceof)
			//Expr_Car e5;
			return donne_valeur_char(e4, table);
		//break;
		default:
			assert (false);
		}
		return '_';
	}

	public ElementInt donne_valeur_int(Expr e, Table_Symbol table) {
		assert (e != null);
		assert (table != null);
		ElementInt res = null;
		//elt=new ElementInt(e1,c1);
		switch (e.op) {
		case Expr.Entier:
			Expr_Entier e2 = (Expr_Entier) e;
			if (e2.str.matches("[\\+-]?[0-9]+")) {
				Integer i = new Integer(e2.str);
				res = new ElementInt(e, i.intValue());
				return res;//i.intValue();
			}
			assert (false);
			break;
		case Expr.Var:
			Expr_Var e3 = (Expr_Var) e;
			Attribut_Complet ac = table.trouve_attribut(e3.nom);
			assert (ac != null);
			if (ac.getFeature() instanceof FeatureExpr) {// feature constante
				Expr e4;
				e4 = ((FeatureExpr) ac.getFeature()).expr;
				assert (e4 != null);
				// TODO : eviter une recursivite infini
				//assert(e4 instanceof)
				//Expr_Car e5;
				ElementInt ei = donne_valeur_int(e4, table);
				return new ElementInt(e, ei.valeur);
			} else if (ac.getFeature() instanceof FeatureUnique) {// feature
																  // unique
				res = new ElementInt(e, table.classe, (FeatureUnique) ac
						.getFeature());
				return res;
			} else {
				assert (false) : e3.nom;
			}
			break;
		case Expr.PlusU:
			Expr_Unaire e5 = (Expr_Unaire) e;
			res = donne_valeur_int(e5.expr1, table);
			//res.valeur=-res.valeur;
			return res;//donne_valeur_int(e5.expr1,table);
		case Expr.MoinsU:
			Expr_Unaire e6 = (Expr_Unaire) e;
			res = donne_valeur_int(e6.expr1, table);
			res.valeur = -res.valeur;
			return res;//-donne_valeur_int(e6.expr1,table);
		default:
			assert (false);
		}
		return null;
	}

	public void verifie_liste_assertion(Classe c, Feature f, Assert ass[],
			int lieux) {
		int i;
		Assert a;
		Type t;

		assert (lieux >= dans_invariant && lieux <= dans_check);
		if (ass == null) {
			return;
		}
		if (lieux == dans_ensure)
			context.dans_ensure = true;
		for (i = 0; i < ass.length; i++) {
			a = ass[i];
			if (a.expr != null) {
				t = verifie_expr(context, a.expr, true);
				if (t != null && !t.egaux(bool)) {//t.nom.compareToIgnoreCase("boolean")
												  // != 0) {
					//erreur("l'assertion n'est pas un booleen",
					// a.expr.debut());
					int no = -1;
					switch (lieux) {
					case dans_require:
						no = ErreurVWBE.type_require;
						break;
					case dans_invariant:
						no = ErreurVWBE.type_invariant;
						break;
					case dans_invariant_boucle:
						no = ErreurVWBE.type_loop_inv;
						break;
					case dans_ensure:
						no = ErreurVWBE.type_ensure;
						break;
					case dans_check:
						no = ErreurVWBE.type_check;
						break;
					default:
						assert (false);
					}
					erreur(new ErreurVWBE(c, a.expr, no));
				}
			}
		}
		if (lieux == dans_ensure)
			context.dans_ensure = false;
	}

	public Type verifie_appel(Context context, NomFeature nom,
			Vector parametre, Classe c0, Position pos, boolean cible) {
		Attribut_Complet a1;
		Type t1, t2;
		int i;
		Classe c; //=context.classe_courante;
		Feature f = env.feature;//context.feature_courante;
		assert (pos != null);
		assert (nom != null);

		if (c0 == null) {
			c = env.classe;//context.classe_courante;
		} else {
			c = c0;
		}
		assert (c != null);
		Table_Symbol table = context.donne_table_symbol(c);

		a1 = context.donne_attribut(nom, c);
		if (a1 != null) {
			assert (table == a1.table_symbol);
			//assert(!nom.nom.equalsIgnoreCase("tata")):"p="+parametre.size()+";p[0]="+parametre.elementAt(0);
			verifie_a_valide(context, nom, parametre, c, pos, a1);
			t1 = a1.donne_type_retour(context);
			if (t1 != null && t1.is_like) {
				int no;
				no = type_like_parametre(t1, table, a1.signature);
				if (no != -1) {// Type de retour like un parametre
					Expr e = (Expr) parametre.elementAt(no);
					if (!e.erreur)
						t1 = e.type;
				}
			}
			return t1;
		} else {
			//assert(table.liste_attribut[7].nom.meme_nom(nom)):
			//		"nom="+nom+";attr="+table.liste_attribut[7].nom;
			//System.out.println(table.toString());
			Declare_entite entite = null;
			DeclareVar var;
			if (nom.nom != null)
				entite = env.donne_entite(nom.nom);
			if (entite != null) {// variable locale ou parametre
				assert (entite instanceof DeclareVar);
				var = (DeclareVar) entite;
				t1 = var.type;
				if (parametre.size() > 0) {
					int nb1, nb2;
					nb1 = (parametre == null) ? 0 : parametre.size();
					nb2 = 0;
					//assert(false):"nb1="+nb1+";nb2="+nb2;
					erreur(new ErreurVUAR1(nom,
							env.classe/* context.classe_courante */, nb1, pos,
							env.classe, nb2, var.debut())
					//"le nombre de parametre ne correspond pas"
					/* ,a1.feature.debut() */);
				}
				return t1;
			} else {
				if (cible)
					logging.erreur(new ErreurVUEX1(c, nom, pos));
				else
					logging.erreur(new ErreurVUEX2(c, nom, pos));
			}

			//fatal
			/* erreur */
			// ("l'attribut " + nom + " n'existe pas dans le classe " + c.nom);
		}
		return null;
	}

	public int type_like_parametre(Type t1, Table_Symbol table1, Signature sig) {
		assert (t1 != null);
		assert (table1 != null);
		assert (sig != null);
		if (t1.is_like) {
			String nom1, nom2;
			nom1 = ((Expr_Var) t1.like).nom;
			for (int i = 0; i < sig.nb_parametre(); i++) {

				nom2 = sig.getNom(i);
				if (nom1.compareToIgnoreCase(nom2) == 0) {
					return i;
				}
			}
		}
		return -1;
	}

	public boolean verifie_a_valide(Context context, NomFeature nom,
			Vector parametre, Classe c, Position pos, Attribut_Complet a1) {
		assert (context != null);
		assert (nom != null);
		assert (parametre != null);
		assert (c != null);
		assert (pos != null);
		assert (a1 != null);
		Table_Symbol table, table2;
		Type d2;
		Vector v1 = parametre;
		Signature signature = a1.signature;
		boolean res = true;
		table = a1.table_symbol;
		assert (table != null);
		if (((signature.nb_parametre() > 0) && (v1 == null || v1.size() == 0))
				|| (v1 != null && signature.nb_parametre() != v1.size())
				|| ((signature.nb_parametre() == 0) && (v1 != null && v1.size() > 0))) {
			int nb1, nb2;
			nb1 = (v1 == null) ? 0 : v1.size();
			nb2 = signature.nb_parametre();
			//assert(false):"nb1="+nb1+";nb2="+nb2;
			erreur(new ErreurVUAR1(nom, env.classe/* context.classe_courante */,
					nb1, pos, c, nb2, a1.getFeature().debut())
			//"le nombre de parametre ne correspond pas"
			/* ,a1.feature.debut() */);
			res = false;
		} else {
			if (v1 != null) {
				Expr e1;
				Type t1, t2, t3;
				table2 = context.donne_table_symbol(c);
				for (int i = 0; i < v1.size(); i++) {
					e1 = (Expr) v1.elementAt(i);
					d2 = signature.getParametre(i);//d[i];
					t1 = verifie_expr(context, e1, true);
					if (t1 == null) {// TODO: type inconnue
						/*
						 * erreur( new ErreurVUAR2(nom,i,
						 * context.classe_courante,t1,e1.debut(),
						 * c,d2,d2.debut()));
						 */
						return false;
					}
					t2 = table.trouve_vrai_type(t1);
					t3 = table2.trouve_vrai_type(d2);
					int no = type_like_parametre(d2, table2, signature);
					//for(int j=0;j<d2.length;j++)
					if (no != -1) {
						Expr e2 = (Expr) v1.elementAt(no);
						t3 = e2.type;
						if (!context.type_compatible(t2, table, t3, table2)) {
							erreur(new ErreurVUAR2(nom, i,
									env.classe/* context.classe_courante */, t2,
									e1.debut(), c, t3, e2.debut())
							/*
							 * "Type incompatible pour le parametre " + i,
							 * e1.debut()
							 */);
							res = false;
						}
					} else {
						if (!context.type_compatible(t2, table, t3, table2)) {
							erreur(new ErreurVUAR2(nom, i,
									env.classe/* context.classe_courante */, t2,
									e1.debut(), c, t3, d2.debut())
							/*
							 * "Type incompatible pour le parametre " + i,
							 * e1.debut()
							 */);
							res = false;
						}
					}
				}
			}
		}
		return false;
	}

	public Type verifie_expr(Context context, Expr expr, boolean debut) {
		Type t = null;
		int i, j;
		NomFeature f1;
		Type t1, t2;
		//Attribut f2 = null;
		//Attribut a1, a2; //attributs,attributs_ancetre
		DeclareVar d[], d2;
		Classe c = env.classe;//context.classe_courante;&é'((è_(-_è
		Feature f = env.feature;//context.feature_courante;
		Table_Symbol table = context.donne_table_symbol(c), table2;
		Attribut_Complet attr1, attr2;

		assert (expr != null);
		switch (expr.op) {
		case Expr.Var:
			Expr_Var expr_var = (Expr_Var) expr;
			if (!context.var_existe(expr_var.nom)) {
				logging.erreur(new ErreurVEEN(c, expr_var.nom, expr_var.oper));
				expr.erreur = true;
				System.out.println("*****a");
			} else {
				if (!context.est_variable(expr_var.nom)) {// il n'existe pas de
														  // type de retour
					erreur(new ErreurVKCN1(c, expr_var));
					expr.erreur = true;
					System.out.println("*****b");
				} else {
					NomFeature f3 = new NomFeature(expr_var.nom);
					Attribut_Complet ac = context.donne_attribut(f3, c);
					if (ac != null) {
						if (ac.signature.nb_parametre() > 0) {// erreur: pas de
															  // parametres
							erreur(new ErreurVUAR1(f3, c, 0, expr_var.debut(),
									c, ac.signature.nb_parametre(), ac
											.getFeature().debut()));
							expr.erreur = true;
							System.out.println("*****c");
						}
					}
					Declare_entite entite = env.donne_entite(expr_var.nom);
					//t =
					// context.donne_type_var(expr_var.nom,expr_var.debut());
					if (entite == null) {
						// TODO: variable sans type de retour
						//erreur("La variable " + expr_var.nom + " n'existe
						// pas");
						//logging.erreur(new
						// ErreurVEEN(c,expr_var.nom,expr_var.oper));
						logging.erreur(new ErreurVEEN(c, expr_var.nom, expr_var
								.debut()));
						expr.erreur = true;
						System.out.println("*****d");
					} else {
						//t=table.trouve_vrai_type(t);
						t = entite.getType();
						assert (t != null);
					}
				}
			}
			expr.type = t;
			expr.classe = env.classe;
			break;
		case Expr.Entier:
			expr.type = t = new TypeSimple(false, "INTEGER", null);
			expr.classe = c;
			break;
		case Expr.Chaine:
			expr.type = t = new TypeSimple(false, "STRING", null);
			expr.classe = c;
			break;
		case Expr.Reel:
			expr.type = t = new TypeSimple(false, "REAL", null);
			expr.classe = c;
			break;
		case Expr.Char:
			expr.type = t = new TypeSimple(false, "CHARACTER", null);
			expr.classe = c;
			break;
		case Expr.Vrai:
		case Expr.Faux:
			expr.type = t = new TypeSimple(false, "BOOLEAN", null);
			expr.classe = c;
			break;
		case Expr.Old:
			//assert(false);
			//t=
			Expr_Unaire expr_un1 = (Expr_Unaire) expr;
			t = verifie_expr(context, expr_un1.expr1, true);
			if (!env.dans_ensure())//(!context.dans_ensure)
			{// Erreur VAOL1
				erreur(new ErreurVAOL1(c, expr_un1));
				expr.erreur = true;
			}
			//erreur("Old non implementé", expr.debut());
			expr.type = t;
			expr.classe = expr_un1.expr1.classe;
			break;
		case Expr.Dollard:
			//assert(false);
			//t=
			Expr_Unaire expr_un2 = (Expr_Unaire) expr;
			t = verifie_expr(context, expr_un2.expr1, true);
			if (expr_un2.expr1.op != Expr.Var)//(!context.dans_ensure)
			{// Erreur VUAR4
				erreur(new ErreurVUAR4(expr_un2.expr1, c, expr.debut()));
				expr.erreur = true;
			}
			//erreur("Old non implementé", expr.debut());
			t = new TypeSimple(false, "POINTER", null);
			expr.type = t;
			expr.classe = c;
			break;
		case Expr.Egal:
		case Expr.Diff:
			Expr_Binaire expr_bin2 = (Expr_Binaire) expr;
			t1 = verifie_expr(context, expr_bin2.expr1, true);
			t2 = verifie_expr(context, expr_bin2.expr2, true);
			if (t1 != null && t2 != null) {
				//Type t1_=table.trouve_vrai_type(t1);
				//Type t2_=table.trouve_vrai_type(t2);
				if (!context.type_compatible(t2, table, t1, table)
						&& !context.type_compatible(t1, table, t2, table)) {
					logging.erreur(new ErreurVWEQ(c, expr_bin2, t1, t2));
					expr.erreur = true;
				}
				t = new TypeSimple(false, "BOOLEAN", new Vector());
			}
			expr.type = t;
			expr.classe = c;
			break;
		case Expr.Plus:
		case Expr.Moins:
		case Expr.Fois:
		case Expr.Div:
		case Expr.Mod:
		case Expr.Puiss:
		case Expr.Xor:
		case Expr.Or:
		case Expr.And:
		case Expr.And_Then:
		case Expr.Or_Else:
		case Expr.Implies:
		case Expr.Infs:
		case Expr.Inf:
		case Expr.Sup:
		case Expr.Sups:
		case Expr.Div_entier:
		case Expr.Free_Op:
			Expr_Binaire expr_bin = (Expr_Binaire) expr;
			f1 = expr_bin.donne_nom_feature();
			//if(f1==null)
			{
				//	erreur("operation");
				//assert(expr_bin.op!=Expr.Free_Op):"f1="+f1;
			}
			//f2 = null;
			t1 = verifie_expr(context, expr_bin.expr1, true);
			t2 = verifie_expr(context, expr_bin.expr2, true);
			boolean trouve = false;
			if (expr_bin.op == 30) {
				String s1 = expr_bin.debut().toString();
				String s2 = "(15,22,.\\std_classe\\COMPARABLE.e)";
				assert (s1 != s2);
				if (s1.equals(s2)) {
					//trouve=true;
					//assert(false):"("+t1+","+t2+")";
				}
			}
			assert (!expr_bin.debut().equals(
					"(15,22,.\\std_classe\\COMPARABLE.e)")) : "expr="
					+ expr_bin.op + "(" + t1 + "," + t2 + ")";
			if (t1 != null && t2 != null) {
				//if(trouve) assert(false);
				Type t1_ = table.trouve_vrai_type(t1);
				Classe cl = context.cherche_classe(t1_);
				if (cl == null) {
					if (trouve)
						assert (false);
					/* context. */
					erreur("La classe1 " + t1.nom + " n'existe pas",
							expr_bin.expr1.debut());
					expr.erreur = true;
				} else {
					//if(trouve) assert(false);
					table2 = context.donne_table_symbol(t1_);
					assert (table2 != null);
					Vector param = new Vector();
					param.addElement(expr_bin.expr2);
					verifie_appel(context, f1, param, cl,
							expr_bin.oper.debut(), true);
					attr1 = table2.donne_attribut(f1);
					if (attr1 == null) {
						//assert(expr_bin.op!=Expr.Free_Op):"f1="+f1;
						t = null;
						expr.erreur = true;
					} else {
						/*
						 * if(!context.est_variable(expr_var.nom)) {// il
						 * n'existe pas de type de retour erreur(new
						 * ErreurVKCN1(c,expr_var)); } TODO: a refaire else
						 */
						{
							t = attr1.donne_type_retour(context);
							if (t == null) {// pas de type de retour
								erreur(new ErreurVKCN1(c, expr_bin));
								expr.erreur = true;
							} else {
								t = table2.trouve_vrai_type(t);
								expr.classe = cl;
							}
						}
					}
				}
			} else {
				//assert(false):"Erreur:type null="+t1+":"+t2+";"+
				//				expr.op+"."+expr.debut();
			}
			if (trouve) {
				assert (false) : "t=" + t;
			}
			expr.type = t;
			break;
		case Expr.Not:
		case Expr.PlusU:
		case Expr.MoinsU:
		case Expr.Free_OpU:
			Expr_Unaire expr_un = (Expr_Unaire) expr;
			f1 = expr_un.donne_nom_feature();
			//assert(false);
			assert (f1 != null);
			//f2 = null;
			t1 = verifie_expr(context, expr_un.expr1, true);
			if (t1 != null) {
				Type t1_ = table.trouve_vrai_type(t1);
				Classe cl = context.cherche_classe(t1_);
				if (cl == null) {
					context.erreur("La classe " + t1.nom + " n'existe pas");
					expr.erreur = true;
				} else {
					table2 = context.donne_table_symbol(t1_);
					assert (table2 != null);
					Vector param = new Vector();
					verifie_appel(context, f1, param, cl, expr_un.oper.debut(),
							true);
					attr1 = table2.donne_attribut(f1);
					if (attr1 == null) {
						t = null;
					} else {
						t = attr1.donne_type_retour(context);
						if (t == null) {// pas de type de retour
							erreur(new ErreurVKCN1(c, expr_un));
							expr.erreur = true;
						} else {
							t = table2.trouve_vrai_type(t);
							expr.classe = cl;
						}
					}
					/*
					 * attr1=table2.donne_attribut(f1); if (attr1 == null) {
					 * /*erreur( "l'operation2 " + f1 + " n'existe pas dans" + "
					 * la classe " + cl.nom,
					 */
					//expr_un./*expr1.*/debut());
					/*
					 * logging.erreur(new ErreurVUEX1(c,f1,expr_un.debut())); }
					 * else { //Feature f3 = f2.feature; if
					 * (/*attr1.parametre!=null&&
					 *///									attr1.parametre.length>0*/attr1.signature.nb_parametre()>0)
					   // {
					/*
					 * context.erreur("parametre incompatible"); } else { t =
					 * attr1.donne_type_retour(context);
					 * t=table2.trouve_vrai_type(t); } }
					 */
				}
			} else {
				//assert(false):"type null="+t1+":"+expr.op+";"+
				//				expr.debut()+"!"+expr_un.expr1.debut()+"!"+
				//				expr_un.expr1.op;
			}
			expr.type = t;
			break;
		case Expr.Appel:
			Expr_Appel expr_app = (Expr_Appel) expr;
			t = verifie_appel(context, new NomFeature(expr_app.nom),
					expr_app.parametre, null, expr_app.debut(), debut);
			//t=table2.trouve_vrai_type(t);
			if (t == null) {// Erreur VKCN1
				Attribut_Complet ac = context.donne_attribut(new NomFeature(
						expr_app.nom), c);
				if (ac != null) {
					erreur(new ErreurVKCN1(c, expr_app));
					expr.erreur = true;
				}
			} else {
				expr.classe = context.cherche_classe(t);
			}
			expr.type = t;
			break;
		case Expr.Tableau:
			Expr_Tableau expr_tab = (Expr_Tableau) expr;
			Vector v = expr_tab.tableau,
			v2;
			Type t3,
			t4 = null;
			System.out.println("debut:");
			for (i = 0; i < v.size(); i++) {
				t3 = verifie_expr(context, (Expr) v.elementAt(i), true);
				System.out.println("type(" + i + ")=" + t3 + ":" + t4);
				// TODO: a calculer le type
				//t4=super_type(t3,t4);
			}
			System.out.println("fin");
			/*
			 * if(t4==null) { assert(false); } v2=new Vector();
			 * v2.addElement(t4);
			 */
			expr.type = t = new TypeSimple(false, "ARRAY", null/*
														  * new
														  * Type(false,"ANY",new
														  * Vector())/*v2
														  */);
			expr.classe = c;
			break;
		case Expr.Point:
			/**
			 * TODO: a revoir (code incorrecte)
			 */
			Expr_Binaire expr_pt = (Expr_Binaire) expr;
			Expr e2 = expr_pt.expr2;
			System.out
					.println("Point-----" + c.nom + ":" + expr_pt/* .expr1+","+e2 */);
			t1 = verifie_expr(context, expr_pt.expr1, debut);
			//assert(t1!=null);
			if (t1 != null) {
				System.out.println("salut1");
				Classe cl = context.cherche_classe(t1);
				if (cl != null) {
					System.out.println("salut2");
					//context.classe_courante = cl;
					if (e2.op != Expr.Appel && e2.op != Expr.Var) {
						context.erreur("Liste d'appel non conforme");
						expr.erreur = true;
					} else {
						//t2 = verifie_expr(context, e2,false);
						//if (t2 != null) {
						System.out.println("salut3");
						String n;
						if (e2.op == Expr.Appel) {
							n = ((Expr_Appel) e2).nom;
						} else {
							n = ((Expr_Var) e2).nom;
						}
						f1 = new NomFeature(n);
						attr1 = context.donne_attribut(f1, cl);
						if (attr1 != null) {
							System.out.println("salut4");
							/*
							 * if(a1.feature.nom.prefix|| a1.feature.nom.infix) {
							 * erreur("la fonction ne doit pas etre prefix ou
							 * infix"); }
							 */
							Signature sig;
							sig = attr1.signature;

							if (e2.op == Expr.Var) {
								if (sig.nb_parametre() > 0) {//	erreur: pas de
															 // parametres
									erreur(new ErreurVUAR1(f1, c, 0,
											e2.debut(), cl, sig.nb_parametre(),
											attr1.getFeature().debut()));
									expr.erreur = true;
								}
								t2 = sig.getTypeRetour();
								assert (t2 != null) : "exp:" + e2 + ","
										+ sig.nb_parametre();
							} else {
								Vector param;
								param = ((Expr_Appel) e2).parametre;
								//if()
								verifie_a_valide(context, f1, param, cl, e2
										.debut(), attr1);
								t2 = attr1.donne_type_retour(context);
								assert (t2 != null);
							}
							table2 = context.donne_table_symbol(t1);
							// tres important car a.b:like x pointe vers le x
							// de la classe A si a:A
							assert (t2 != null);
							t = table2.trouve_vrai_type(t2);
							//t = t2;
							expr.classe = cl;
						} else {
							System.out.println("Salut5");
							/* context. */
							/*
							 * erreur( "la fonction " + n + " n'existe pas dans
							 * la classe " + t1.nom, expr_pt.debut());
							 */
							erreur(new ErreurVUEX2(cl, f1, e2.debut()));
							expr.erreur = true;
						}
						//}
					}
					//context.classe_courante = c;
				}
			}
			expr.type = t;
			System.out.println("Fin point");
			break;
		default:
			assert (false);
			/* context. */
			erreur("expression no " + expr.op + " inconnue", expr.debut());
			break;
		}
		//assert(t==null/*||!t.is_like*/):"t="+t+",expr="+expr;
		return t;
	}

	// retourne le plus petit type qui est conforme a t1 et t2
	public Type super_type(Type t1, Type t2) {
		if (t1 == null)
			return t2;
		if (t2 == null)
			return t1;
		Classe c1, c2;
		int no1, no2, i, len, res = -1, j;
		boolean table[];
		Type t;
		c1 = context.cherche_classe(t1);
		no1 = context.no_classe(c1);
		c2 = context.cherche_classe(t2);
		no2 = context.no_classe(c2);
		len = table_heritage.length;
		table = new boolean[len];
		System.out.println("super1");
		for (i = 0; i < len; i++) {
			table[i] = table_heritage[no1][i] && table_heritage[no2][i];
		}
		System.out.println("super2");
		for (i = 0; i < len; i++) {
			int nb_descendant = 0, nb_total = 0;
			if (table[i]) {// un des ancetres
				System.out.println("super5:" + i);
				for (j = 0; j < len; j++) {
					if (j != i && table[j]) {//un autre des ancetres
						nb_total++;
						if (table_heritage[j][i]) {
							nb_descendant++;
						}
					}
				}
				System.out.println("super6:" + i);
				if (nb_total > 0 && nb_total == nb_descendant) {// la classe i
																// est l'ancetre
																// de toutes les
																// classes
					table[i] = false;
					i = 0;
				}
				System.out.println("super7:" + i);
			}
		}
		System.out.println("super3");
		for (i = 0; i < len; i++) {
			if (table[i]) {
				assert (res == -1) : "t=" + res + "+" + i;// plusieurs type de
														  // retour non compris
				res = i;
			}
		}
		assert (res != -1);
		System.out.println("super4");
		t = ((Classe) liste_classe.elementAt(res)).type;
		return t;
	}

	public void affiche_heritage_directe() {
		int i, j, nb;
		Classe cl, cl2;

		System.out.println("heritage directe:");
		for (i = 0; i < liste_classe.size(); i++) {
			cl = (Classe) liste_classe.elementAt(i);
			System.out.print("classe " + cl.nom + " herite de :");
			nb = 0;
			for (j = 0; j < table_heritage_directe.length; j++) {

				cl2 = (Classe) liste_classe.elementAt(j);
				if (table_heritage_directe[i][j]) {
					if (nb > 0) {
						System.out.print(",");
					}
					System.out.print(cl2.nom);
					nb++;
				}
			}
			System.out.println("");
			//System.out.println("attr:" + cl.attributs);
			//System.out.println("attr ancetre:" + cl.attributs_ancetre);
		}
	}

	public void affiche_heritage() {
		int i, j, nb;
		Classe cl, cl2;

		System.out.println("heritage complet:");
		for (i = 0; i < table_heritage.length; i++) {
			cl = (Classe) liste_classe.elementAt(i);
			System.out.print("classe " + cl.nom + " herite de :");
			nb = 0;
			for (j = 0; j < table_heritage.length; j++) {

				cl2 = (Classe) liste_classe.elementAt(j);
				if (table_heritage[i][j]) {
					if (nb > 0) {
						System.out.print(",");
					}
					System.out.print(cl2.nom);
					nb++;
				}
			}
			System.out.println("");
			//System.out.println("attr:" + cl.attributs);
			//System.out.println("attr ancetre:" + cl.attributs_ancetre);
		}
	}

	public void affiche_classe() {
		int i;
		Classe cl;
		for (i = 0; i < liste_classe.size(); i++) {
			cl = (Classe) liste_classe.elementAt(i);
			System.out.println(i + ":" + cl.nom);
		}
	}

	public void ajoute_type(Type liste[]) {
		int i, j;
		Type t, t2;
		boolean trouve = false;

		for (i = 0; i < liste.length; i++) {
			t = liste[i];
			trouve = false;
			for (j = 0; !trouve && j < liste_type.size(); j++) {
				t2 = (Type) liste_type.elementAt(j);
				if (t2.egaux(t)) {
					trouve = true;
				}
			}
			if (!trouve) {
				liste_type.addElement(t);
			}
		}
	}

	public Ace get_configuration() {
		return configuration;
	}

	protected Ace configuration;

	public void configure(String nom,String chemins[]) {
		assert (nom != null);
		if (nom.endsWith(".e")||nom.endsWith(".E")) {// fichier Eiffel
			Grappe g;
			NomGrappe ng;
			NomSysteme ns = new NomSysteme("defaut", new Token(-1, -1, "xx",
					"yy"));
			Type nom_classe;
			String nom_simple;
			Vector liste;
			Chaine rep;
			String path_standard[] = { "", ".\\std_classe\\", ".\\"//,/* ".\\test\\", */
					/*""*/ };
			if(chemins!=null)
			{
				path_standard=chemins;
			}
			assert (path_standard != null);
			assert (path_standard.length > 0);
			File f = new File(nom);
			String path = f.getParent();
			nom_simple = f.getName();
			if (nom_simple.endsWith(".e")||nom_simple.endsWith(".E"))
				nom_simple = nom_simple.substring(0, nom_simple.length() - 2);
			nom_classe = new TypeSimple(false, nom_simple, new Vector());
			if (path != null) {
				String tmp[];
				tmp=new String[path_standard.length+1];
				for(int i=0;i<path_standard.length;i++)
				{
					tmp[i]=path_standard[i];
				}
				tmp[path_standard.length] = path + "\\";
				path_standard=tmp;
			}
			liste = new Vector();
			for (int i = 0; i < path_standard.length; i++) {
				Vector v = new Vector();
				v.addElement(path_standard[i]);
				rep = new Chaine(v, new Token(-1, -1, "xx", "yy"));
				g = new Grappe(null, rep);
				liste.addElement(g);
			}
			configuration = new Ace(ns, nom_classe, null, null, liste,null,null,path);
		} else if (nom.endsWith(".ace")) {// fichier Ace
			String nom_reel = nom;
			Ace ace;
			System.out.println("++++++++++++++++++++++++");
			try{
			ParserAce parser_tmp,parser = new ParserAce(nom_reel);
			//parser.setASTNodeClass("compiler.ASTDefaut");
			//parser.setFilename(nom_reel);
			//parser.lexer = lexer;
			parser.logging = logging;
			parser_tmp = parser;
			//parser.logging=logging;
			// Parse the input expression
			//System.out.println("A1");
			ace = parser.parse_ace();
			if (ace == null || !parser.etatOk() || !parser.etatParserOk()) {
				configuration=null;
				if (!parser.etatOk()) {
					int x, y;
					x = parser_tmp.lexer.getColumn();
					y = parser_tmp.lexer.getLine();
					logging.erreur(new ErreurSource("Erreur lexicale:"
							+ parser.lexer.getMessageErr(), x, y, nom_reel));
				} else if (!parser.etatParserOk()) {
					logging.erreur(new ErreurSource("Erreur de parsing:"
							+ parser.getMsgErreur(), parser.getLine(), parser
							.getColumn(), parser.getFilename()));
				} else {
					assert (false);
				}
				return;
			}
			configuration=ace;
			
		}catch (FileNotFoundException e) {
			logging.erreur(new ErreurIO("Fichier " + nom_reel
					+ " non trouve", nom_reel));
			return;
		} catch (SecurityException e) {
			logging.erreur(new ErreurSource("Erreur: erreur de securite", -1,
					-1, nom_reel));
			return;
		}
			/*try {
				AceLexer lexer = new AceLexer(new FileInputStream(nom_reel));
				lexer.setFilename(nom_reel);
				AceParser parser = new AceParser(lexer);
				parser.setASTNodeClass("tinyeiffel.compiler.ASTDefaut");
				parser.setFilename(nom_reel);
				parser.lexer = lexer;
				parser.logging = logging;
				//parser.logging=logging;
				// Parse the input expression
				configuration = parser.ace();
				System.out.println("classe racine:" + configuration.nom_classe);
				System.out.println("----------------------");
			} catch (TokenStreamException e) {
				//erreur("exception: " + e);
				int x, y;
				x = -1;
				y = -1;
				if (e instanceof TokenStreamRecognitionException) {
					//x=((TokenStreamRecognitionException)e).;
					//y=((TokenStreamRecognitionException)e);
				}
				logging.erreur(new ErreurSource("Erreur lexicale:"
						+ e.getMessage(), x, y, nom_reel));
			} catch (RecognitionException e) {
				//erreur("exception: " + e);
				logging.erreur(new ErreurSource("Erreur de parsing:"
						+ e.getMessage(), e.getLine(), e.getColumn(), e
						.getFilename()));
			} catch (FileNotFoundException e) {
				//erreur("Erreur1: fichier " + nom_fichier + " non trouve");
				//erreur("exception: " + e);
				//assert(false);
				logging.erreur(new ErreurIO("Fichier " + nom + " non trouve",
						nom));
			} catch (SecurityException e) {
				logging.erreur(new ErreurSource("Erreur: erreur de securite",
						-1, -1, nom));
			}*/
		} else {
			assert (false);
		}
		if (configuration == null) {
			System.out.println("Univers:null");
		} else {
			Grappe g, liste[];
			liste = configuration.liste_grappe;
			/*
			 * System.out.println("Univers:{"); if(liste!=null) { for(int i=0;i
			 * <liste.length;i++) { if(i>0) System.out.println(","); g=liste[i];
			 * System.out.print(i+":"+g.donne_repertoire());
			 * if(g.getNom()!=null) { System.out.print("("+g.getNom()+")"); } } }
			 * System.out.println("}");
			 */
		}
	}

	public String donne_nom(String nom) {
		File file;
		String path[] = null; //= path_standard;
		int i;
		String s;
		Grappe g[];

		assert (configuration != null);
		g = configuration.liste_grappe;
		path = new String[g.length];
		for (i = 0; i < g.length; i++) {
			path[i] = g[i].donne_repertoire();
			if (!path[i].endsWith("\\"))
				path[i] = path[i] + "\\";
		}
		for (i = 0; i < path.length; i++) {
			s = path[i] + nom;
			if (nom.equalsIgnoreCase("TOTO1.e")) {
				System.out.println("coucou[" + i + "]" + s + ";");
			}
			file = new File(s);
			if (file.exists()) {
				return s;
			}
		}

		return "";
	}

	public boolean classe_existe(String nom) {
		return donne_nom(nom + ".e") != "";
	}

	public Classe parse_file(String nom_fichier) {
		Classe c;
		String nom_reel = null;
		Parser parser_tmp = null;
		System.out.println("parse de " + nom_fichier);
		nom_reel = donne_nom(nom_fichier);
		if (nom_reel == "") {
			logging.erreur(new ErreurIO("Fichier " + nom_fichier
					+ " non trouve", nom_fichier));
			return null;
		}
		try {
			//Lexer lexer =new Lexer(new FileInputStream(nom_reel));
			//lexer.setFilename(nom_reel);
			Parser parser = new Parser(nom_reel);
			//parser.setASTNodeClass("compiler.ASTDefaut");
			//parser.setFilename(nom_reel);
			//parser.lexer = lexer;
			parser.logging = logging;
			parser_tmp = parser;
			//parser.logging=logging;
			// Parse the input expression
			//System.out.println("A1");
			c = parser.parse_classe();
			if (c == null || !parser.etatOk() || !parser.etatParserOk()) {
				if (!parser.etatOk()) {
					int x, y;
					x = parser_tmp.lexer.getColumn();
					y = parser_tmp.lexer.getLine();
					logging.erreur(new ErreurSource("Erreur lexicale:"
							+ parser.lexer.getMessageErr(), x, y, nom_reel));
				} else if (!parser.etatParserOk()) {
					logging.erreur(new ErreurSource("Erreur de parsing:"
							+ parser.getMsgErreur(), parser.getLine(), parser
							.getColumn(), parser.getFilename()));
				} else {
					assert (false);
				}
				return null;
			}
			//System.out.println("A2");
			Vector liste_classe = parser.type_utilisee;
			//System.out.println("classe:" + liste_classe);
			if (c == null) {
				return null;
			}
			if (c.type != null && c.type.generique != null
					&& c.type.generique.length > 0) {// elimination dans
													 // liste_classe
				// des parametres generiques
				for (int i = 0; i < c.type.generique.length; i++) {
					Type t1, t2;
					t1 = c.type.generique[i];
					for (int j = 0; j < liste_classe.size(); j++) {
						t2 = (Type) liste_classe.elementAt(j);
						if (t1.egaux(t2)) {
							liste_classe.remove(j);
							j--;
						}
					}
				}
			}
			c.set_liste_classe(liste_classe);
		}/*
		  * catch (TokenStreamException e) { //erreur("exception: " + e); int
		  * x,y;
		  * 
		  * x=parser_tmp.lexer.getColumn(); y=parser_tmp.lexer.getLine();
		  * //parser_tmp.lexer.getFilename(); if(e instanceof
		  * TokenStreamRecognitionException) {
		  * //x=((TokenStreamRecognitionException)e).;
		  * //y=((TokenStreamRecognitionException)e); } logging.erreur(new
		  * ErreurSource("Erreur lexicale:"+ e.getMessage(),x,y,nom_reel));
		  * return null; } catch (RecognitionException e) { //erreur("exception: " +
		  * e); logging.erreur(new ErreurSource("Erreur de parsing:"+
		  * e.getMessage(),e.getLine(),e.getColumn(), e.getFilename())); return
		  * null; }
		  */catch (FileNotFoundException e) {
			//erreur("Erreur1: fichier " + nom_fichier + " non trouve");
			//erreur("exception: " + e);
			//assert(false);
			logging.erreur(new ErreurIO("Fichier " + nom_fichier
					+ " non trouve", nom_fichier));
			return null;
		} catch (SecurityException e) {
			logging.erreur(new ErreurSource("Erreur: erreur de securite", -1,
					-1, nom_fichier));
			return null;
		}
		return c;
	}

	public Classe parse_file2(String nom_fichier) {
		Classe c;
		String nom_reel = null;
		EiffelParser parser_tmp = null;
		System.out.println("parse de " + nom_fichier);
		nom_reel = donne_nom(nom_fichier);
		if (nom_reel == "") {
			logging.erreur(new ErreurIO("Fichier " + nom_fichier
					+ " non trouve", nom_fichier));
			return null;
		}
		try {
			EiffelLexer lexer = new EiffelLexer(new FileInputStream(nom_reel));
			lexer.setFilename(nom_reel);
			EiffelParser parser = new EiffelParser(lexer);
			parser.setASTNodeClass("tinyeiffel.compiler.ASTDefaut");
			parser.setFilename(nom_reel);
			parser.lexer = lexer;
			parser.logging = logging;
			parser_tmp = parser;
			//parser.logging=logging;
			// Parse the input expression
			//System.out.println("A1");
			c = parser.classe();
			//System.out.println("A2");
			Vector liste_classe = parser.type_utilisee;
			//System.out.println("classe:" + liste_classe);
			if (c == null) {
				return null;
			}
			if (c.type != null && c.type.generique != null
					&& c.type.generique.length > 0) {// elimination dans
													 // liste_classe
				// des parametres generiques
				for (int i = 0; i < c.type.generique.length; i++) {
					Type t1, t2;
					t1 = c.type.generique[i];
					for (int j = 0; j < liste_classe.size(); j++) {
						t2 = (Type) liste_classe.elementAt(j);
						if (t1.egaux(t2)) {
							liste_classe.remove(j);
							j--;
						}
					}
				}
			}
			c.set_liste_classe(liste_classe);
		} catch (TokenStreamException e) {
			//erreur("exception: " + e);
			int x, y;

			x = parser_tmp.lexer.getColumn();
			y = parser_tmp.lexer.getLine();
			//parser_tmp.lexer.getFilename();
			if (e instanceof TokenStreamRecognitionException) {
				//x=((TokenStreamRecognitionException)e).;
				//y=((TokenStreamRecognitionException)e);
			}
			logging.erreur(new ErreurSource(
					"Erreur lexicale:" + e.getMessage(), x, y, nom_reel));
			return null;
		} catch (RecognitionException e) {
			//erreur("exception: " + e);
			logging.erreur(new ErreurSource("Erreur de parsing:"
					+ e.getMessage(), e.getLine(), e.getColumn(), e
					.getFilename()));
			return null;
		} catch (FileNotFoundException e) {
			//erreur("Erreur1: fichier " + nom_fichier + " non trouve");
			//erreur("exception: " + e);
			//assert(false);
			logging.erreur(new ErreurIO("Fichier " + nom_fichier
					+ " non trouve", nom_fichier));
			return null;
		} catch (SecurityException e) {
			logging.erreur(new ErreurSource("Erreur: erreur de securite", -1,
					-1, nom_fichier));
			return null;
		}
		return c;
	}
}