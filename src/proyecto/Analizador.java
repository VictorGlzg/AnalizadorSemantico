package proyecto;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Analizador{
    public static void main(String[] args) throws Exception{
        String ruta = "../src/proyecto/Lex.flex"; //ruta lexer.flex
        String ruta2 = "../src/proyecto/LexerCup.flex"; //ruta lexerCup.flex
        String[] rutaS={"-parser","Sintax","../src/proyecto/Sintax.cup"};
        gen(ruta,ruta2,rutaS);
    }
    public static void gen(String ruta1, String ruta2, String[] rutaS) throws IOException, Exception{
        File arch;
        arch = new File(ruta1);
        JFlex.Main.generate(arch);
        arch = new File(ruta2);
        JFlex.Main.generate(arch);
        java_cup.Main.main(rutaS);
        
        Path rutaSym = Paths.get("../src/proyecto/sym.java");
        if(Files.exists(rutaSym)){
            Files.delete(rutaSym);
        }
        Files.move(
                Paths.get("../sym.java"), 
                Paths.get("../src/proyecto/sym.java")
        );
        Path rutaSin = Paths.get("../src/proyecto/Sintax.java");
        if(Files.exists(rutaSin)){
            Files.delete(rutaSin);
        }
        Files.move(
                Paths.get("../Sintax.java"), 
                Paths.get("../src/proyecto/Sintax.java")
        );
    }
}