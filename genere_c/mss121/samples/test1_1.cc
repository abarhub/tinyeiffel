//-------------------------------------------------------
//MSS sample program #1: 'A simple and buggy String class'
//-------------------------------------------------------

/****************************************************************************
This is sample program #1 for the MSS package. Read the file 'test1.txt'
for more info. Note that this program is delibelately buggy!
****************************************************************************/

#include <stdlib.h>
#include <iostream.h>
#include <mss.h> //Let's invite MSS to this party ;)

/* DECLARATION */
/* ----------- */
class String
{
  char *str;
public:
  String();
  String(char *);
  ~String();
  unsigned Length();
  bool is_equal_to(String);
};


/* IMPLEMENTATION */
/* -------------- */

//CONSTRUCTOR
String::String()
{
   str=NULL;
}

//CONSTRUCTOR 2
String::String(char *strpar)
{
   unsigned length=0;
   char *aux1,*aux2=strpar;
   //count the length of the source string
   while(*aux2!=0)
   {
      length++;  //add one more
      aux2++;  //step to the next char
   }
  //allocate space for the string
  aux1=str=new char [length];
  //copy the string until \0 found.
  aux2=strpar;
  while(*aux2!=0)
    *aux1++=*aux2++;

  //add the \0 terminator
  *aux1=0;
}

//DESTRUCTOR
String::~String()
{
  delete [] str;
}

//Returns the lenght of the string not counting the \0 terminator
unsigned String::Length()
{
   unsigned length=0;
   char *aux=str;
  //count until '\0'(end of string) found.
  if(aux!=NULL)
     while(*aux!=0)
      {
         length++;  //add one more
         aux++;     //step to the next char
      }
   return length;
}

//returns 'true' if strings are equal
bool String::is_equal_to(String strpar)
{
   bool equal=false;
   char *aux1=str, *aux2=strpar.str;
   //compare only if same length
   if(this->Length()==strpar.Length())
      while(*aux1!=0 && (equal=(*aux1++==*aux2++)));
   return equal;
}


/* MAIN PROGRAM */
/*--------------*/

int main()
{

   String str1;
   String str2("Hello Underworld!");
   String str3("Hello Underworld!");
   if(str2.is_equal_to(str3))
      cout<<"equal"<<endl;
   else
      cout<<"different"<<endl;
   cout<<"String length is: "<<str3.Length()<<endl;

}


