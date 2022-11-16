package proyecto;
import java_cup.runtime.Symbol;
%%
%class LexerCup
%type java_cup.runtime.Symbol
%cup
%full
%line
%char
L=[a-zA-Z_]+
N=[0-9]+
ND=[.]
espacio=[ ,\t,\r,\n]+
%{
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    }
    private Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    }
%}
%%

//----Tipos de datos----
 entero {return new Symbol(sym.Entero, yychar, yyline, yytext());}
 flotante {return new Symbol(sym.Flotante, yychar, yyline, yytext());}
 boleano {return new Symbol(sym.Boleano, yychar, yyline, yytext());}
 cadena {return new Symbol(sym.Cadena, yychar, yyline, yytext());} 

//----Palabras reservadas----
para {return new Symbol(sym.Para, yychar, yyline, yytext());}
cambio {return new Symbol(sym.Cambio, yychar, yyline, yytext());}
si {return new Symbol(sym.Si, yychar, yyline, yytext());}
ademas {return new Symbol(sym.Ademas, yychar, yyline, yytext());}
mientras {return new Symbol(sym.Mientras, yychar, yyline, yytext());}
publico {return new Symbol(sym.Publico, yychar, yyline, yytext());}
inicial {return new Symbol(sym.Inicial, yychar, yyline, yytext());}
caso {return new Symbol(sym.Caso, yychar, yyline, yytext());}
salida {return new Symbol(sym.Salida, yychar, yyline, yytext());}
sino {return new Symbol(sym.Sino, yychar, yyline, yytext());}

//----Espacios a ignorar----
{espacio} {/*Ignore*/}
"//" {/*Ignore*/}

//----Simbolos----
"'" {return new Symbol(sym.Comillas, yychar, yyline, yytext());}
"=" {return new Symbol(sym.Igual, yychar, yyline, yytext());}
"^" {return new Symbol(sym.Exponente, yychar, yyline, yytext());}
"+" {return new Symbol(sym.Suma, yychar, yyline, yytext());}
"-" {return new Symbol(sym.Resta, yychar, yyline, yytext());}
"*" {return new Symbol(sym.Multiplicacion, yychar, yyline, yytext());}
"/" {return new Symbol(sym.Division, yychar, yyline, yytext());}
"#" {return new Symbol(sym.LlaveAC, yychar, yyline, yytext());}
"&" {return new Symbol(sym.CerrarL, yychar, yyline, yytext());}
"[" {return new Symbol(sym.Apertura, yychar, yyline, yytext());}
"]" {return new Symbol(sym.Cerradura, yychar, yyline, yytext());}
"@" {return new Symbol(sym.Arroba, yychar, yyline, yytext());}
":" {return new Symbol(sym.DoblePunto, yychar, yyline, yytext());}

//----Operadores Relacionales----
( ">" | "<" | ">=" | "<=" | "==" | "<>" ) {return new Symbol(sym.Op_R, yychar, yyline, yytext());}

//----Operadores Lógicos----
( "&&" | "||" | "!" | "|" ) {return new Symbol(sym.Op_L, yychar, yyline, yytext());}

//----Operador Booleano----
( verdadero | falso ) {return new Symbol(sym.Op_B, yychar, yyline, yytext());}

//----Operadores Incremento y Decremento----
( "++" | "--" ) {return new Symbol(sym.Op_I, yychar, yyline, yytext());}

{L}({L}|{N})* {return new Symbol(sym.Identificador, yychar, yyline, yytext());} //Regla sintaxica
{N} {return new Symbol(sym.Numero, yychar, yyline, yytext());}
({N}{ND}{N})|({N}|{ND}{N}) {return new Symbol(sym.Decimal, yychar, yyline, yytext());}
 . {return new Symbol(sym.ERROR, yychar, yyline, yytext());}