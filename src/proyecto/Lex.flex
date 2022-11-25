//Toda regla lexica pertenece aquí
package proyecto;
import static proyecto.Tokens.*; //clase numeración que incluye todos los tokens
%%
%class Lex
%type Tokens
L=[a-zA-Z_]+
N=[0-9]+
ND=[.]
espacio=[ ,\t,\r]+
%{
    public String lexeme;
%}
%%

//----Tipos de datos----
entero {lexeme=yytext(); return Entero;}
flotante {lexeme=yytext(); return Flotante;}
boleano {lexeme=yytext(); return Boleano;}
cadena {lexeme=yytext(); return Cadena;}

//----Palabras reservadas----
para {lexeme=yytext(); return Para;}
cambio {lexeme=yytext(); return Cambio;}
si {lexeme=yytext(); return Si;}
ademas {lexeme=yytext(); return Ademas;}
mientras {lexeme=yytext(); return Mientras;}
publico {lexeme=yytext(); return Publico;}
inicial {lexeme=yytext(); return Inicial;}
caso {lexeme=yytext(); return Caso;}
salida {lexeme=yytext(); return Salida;}
sino {lexeme=yytext(); return Sino;}

//----Espacios a ignorar----
{espacio} {/*Ignore*/}
"//" {/*Ignore*/}

//----Simbolos----
"\n" {return Linea;}
"'" {lexeme=yytext(); return Comillas;}
"=" {lexeme=yytext(); return Igual;}
"^" {lexeme=yytext(); return Exponente;}
"+" {lexeme=yytext(); return Suma;}
"-" {lexeme=yytext(); return Resta;}
"*" {lexeme=yytext(); return Multiplicacion;}
"/" {lexeme=yytext(); return Division;}
"#" {lexeme=yytext(); return LlaveAC;}
"&" {lexeme=yytext(); return CerrarL;}
"[" {lexeme=yytext(); return Apertura;}
"]" {lexeme=yytext(); return Cerradura;}
"@" {lexeme=yytext(); return Arroba;}
":" {lexeme=yytext(); return DoblePunto;}
 
//----Operadores Relacionales----
( ">" | "<" | ">=" | "<=" | "==" | "<>" ) {lexeme=yytext(); return Op_R;}

//----Operadores Lógicos----
( "&&" | "||" | "!" | "|" ) {lexeme=yytext(); return Op_L;}

//----Operador Booleano----
( verdadero | falso ) {lexeme = yytext(); return Op_B;}

//----Operadores Incremento y Decremento----
( "++" | "--" ) {lexeme=yytext(); return Op_I;}

{L}({L}|{N})* {lexeme=yytext(); return Identificador;} //Regla sintaxica
{N} {lexeme=yytext(); return Numero;}
({N}{ND}{N})|({N}|{ND}{N}) {lexeme=yytext(); return Decimal;}
("\'"{L}*({espacio}*{L}*)"\'") {lexeme=yytext(); return Texto;}
 . {return ERROR;}