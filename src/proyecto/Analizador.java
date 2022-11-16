package proyecto;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Analizador{
    public static void main(String[] args) throws Exception{
        String ruta = "D:/TEC/6to semestre/Java/AnalizadorLexico/src/proyecto/Lex.flex"; //ruta lexer.flex
        String ruta2 = "D:/TEC/6to semestre/Java/AnalizadorLexico/src/proyecto/LexerCup.flex"; //ruta lexerCup.flex
        String[] rutaS={"-parser","Sintax","D:/TEC/6to semestre/Java/AnalizadorLexico/src/proyecto/Sintax.cup"};
        gen(ruta,ruta2,rutaS);
    }
    public static void gen(String ruta1, String ruta2, String[] rutaS) throws IOException, Exception{
        File arch;
        arch = new File(ruta1);
        JFlex.Main.generate(arch);
        arch = new File(ruta2);
        JFlex.Main.generate(arch);
        java_cup.Main.main(rutaS);
        
        Path rutaSym = Paths.get("D:/TEC/6to semestre/Java/AnalizadorLexico/src/proyecto/sym.java");
        if(Files.exists(rutaSym)){
            Files.delete(rutaSym);
        }
        Files.move(
                Paths.get("D:/TEC/6to semestre/Java/AnalizadorLexico/sym.java"), 
                Paths.get("D:/TEC/6to semestre/Java/AnalizadorLexico/src/proyecto/sym.java")
        );
        Path rutaSin = Paths.get("D:/TEC/6to semestre/Java/AnalizadorLexico/src/proyecto/Sintax.java");
        if(Files.exists(rutaSin)){
            Files.delete(rutaSin);
        }
        Files.move(
                Paths.get("D:/TEC/6to semestre/Java/AnalizadorLexico/Sintax.java"), 
                Paths.get("D:/TEC/6to semestre/Java/AnalizadorLexico/src/proyecto/Sintax.java")
        );
    }
}