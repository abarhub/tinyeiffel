@echo off
rem execution d'un programme
echo debut
echo >resultat.log
%1 %2 %3
if errorlevel -1 goto fin
echo Il y a des erreurs
echo Erreur >>resultat.log

:fin
echo fin