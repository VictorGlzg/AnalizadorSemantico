package proyecto;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import jdk.nashorn.internal.runtime.regexp.joni.Syntax;

/*
- ARREGLAR LAS STRINGS PARA CREAR EL TOKEN TEXTO.
- COMPROBAR TIPO DE DATO EN ASIGNACIÓN.
- ARREGLAR SINTACTICO ASIGNACIÓN DE VARIABLE.

COMPLETADAS:
- CHECAR LOS DECIMALES.
-SEPARAR LÉXICO DEL SEMÁNTICO.
-CREAR FRAME PARA TABLA DE SIMBOLOS.
*/

public class InterfazAnalizador extends javax.swing.JFrame {
JFrameTablaSimbolos  jf = new JFrameTablaSimbolos();
DefaultTableModel tblModel = (DefaultTableModel)jf.tablaSimbolos.getModel();
ArrayList<objetoTabla> tablaSimbolos = new ArrayList<objetoTabla>();
String var, valor;
Tokens tipo;
Boolean declaracion = false, fin = false, valAsig = false, error = false ,asignacion = false;
String semant="";
    /**
     * Creates new form InterfazAnalizador
     */
    public InterfazAnalizador() {
        initComponents();
        Limpiar();
        this.setLocationRelativeTo(null);
    }
    
    private void analizarLex() throws IOException{
        int cont = 1;
        
        String expr = (String) txtAnalizar.getText();
        //System.out.println(expr);
        Lex lexer = new Lex(new StringReader(expr));
        String resultado = "LINEA " + cont + "\t\tSIMBOLO\n";
        

        while (true) {
            Tokens token = lexer.yylex();
            //System.out.println(token);
            if (token == null) {
                txtAnalizado.setText(resultado);
                return;
            }
            switch (token) {
                case Linea:
                    cont++;
                    resultado += "LINEA " + cont + "\n";
                    break;
                case Comillas:
                    resultado += "  <Comillas>\t\t" + lexer.lexeme + "\n";
                    break;
                case Entero:
                    resultado += "  <Tipo de dato>\t" + lexer.lexeme + "\n";
                    tipo = token;
                    declaracion = true;
                    break;
                case Flotante:
                    resultado += "  <Tipo de dato>\t" + lexer.lexeme + "\n";
                    tipo = token;
                    declaracion = true;
                    break;
                case Boleano:
                    resultado += "  <Tipo de dato>\t" + lexer.lexeme + "\n";
                    tipo = token;
                    declaracion = true;
                    break;
                case Cadena:
                    resultado += "  <Tipo de dato>\t" + lexer.lexeme + "\n";
                    break;
                case Si:
                    resultado += "  <Reservada if>\t" + lexer.lexeme + "\n";
                    break;
                case Sino:
                    resultado += "  <Reservada if else>\t" + lexer.lexeme + "\n";
                    break;
                case Ademas:
                    resultado += "  <Reservada else>\t" + lexer.lexeme + "\n";
                    break;
                case Mientras:
                    resultado += "  <Reservada while>\t" + lexer.lexeme + "\n";
                    break;
                case Para:
                    resultado += "  <Reservada for>\t" + lexer.lexeme + "\n";
                    break;
                case Igual:
                    resultado += "  <Operador igual>\t" + lexer.lexeme + "\n";
                    break;
                case Suma:
                    resultado += "  <Operador suma>\t" + lexer.lexeme + "\n";
                    break;
                case Resta:
                    resultado += "  <Operador resta>\t" + lexer.lexeme + "\n";
                    break;
                case Multiplicacion:
                    resultado += "  <Operador multiplicacion>\t" + lexer.lexeme + "\n";
                    break;
                case Division:
                    resultado += "  <Operador division>\t" + lexer.lexeme + "\n";
                    break;
                case Op_L:
                    resultado += "  <Operador logico>\t" + lexer.lexeme + "\n";
                    break;
                case Op_R:
                    resultado += "  <Operador relacional>\t" + lexer.lexeme + "\n";
                    break;
                    //BOLEANO-------------------
                case Op_B:
                    resultado += "  <Operador booleano>\t" + lexer.lexeme + "\n";
                    if(declaracion||asignacion){
                    valor = lexer.lexeme;
                    valAsig = true;
                    }
                    break;
                case Apertura:
                    resultado += "  <Parentesis de apertura>\t" + lexer.lexeme + "\n";
                    break;
                case Cerradura:
                    resultado += "  <Parentesis de cierre>\t" + lexer.lexeme + "\n";
                    break;
                case LlaveAC:
                    resultado += "  <Llave de apertura/cerradura>\t" + lexer.lexeme + "\n";
                    break;
                case Inicial:
                    resultado += "  <Reservada main>\t" + lexer.lexeme + "\n";
                    break;
                //punto y coma----------------
                case CerrarL:
                    resultado += "  <Punto y coma>\t" + lexer.lexeme + "\n";
                    if(declaracion||asignacion)
                    fin = true;
                    break;
                // IDENTIFICADOR ----------------------------------------
                case Identificador:
                    if(tipo==null) //CORREGIR
                        asignacion = true;
                    else
                        asignacion = false;
                    checarTablaSimbolos(lexer.lexeme);
                    resultado += "  <Identificador>\t" + lexer.lexeme + "\n";
                    if(declaracion||asignacion)
                    var = lexer.lexeme;
                    break;
                case Numero:
                    resultado += "  <Numero>\t\t" + lexer.lexeme + "\n";
                    if(declaracion||asignacion){
                    valor = lexer.lexeme;
                    valAsig = true;
                    }
                    break;
                case Decimal:
                    resultado += "  <Numero decimal>\t" + lexer.lexeme + "\n";
                    if(declaracion||asignacion){
                    valor = lexer.lexeme;
                    valAsig = true;
                    }
                    break;
                case Arroba:
                    resultado += "  <Arroba>\t" + lexer.lexeme + "\n";
                    break;
                case Op_I:
                    resultado += "  <Operador Incremento y Decremento>\t" + lexer.lexeme + "\n";
                    break;
                case Caso:
                    resultado += "  <Case 'x'>\n";
                    break;
                case Salida:
                    resultado += "  <Break>\n";
                    break;
                case ERROR:
                    resultado += "  <Simbolo no definido>\n";
                    break;  
                default:
                    resultado += "  < " + lexer.lexeme + " >\n";
                    break;
            }
            if(fin){ //Termina de analizar la linea. Encontró un punto y coma &
            
            if(declaracion){ //Se esta declarando y creando en la tabla de simbolos.
                objetoTabla aux;
                if(valAsig){ //Existe valor asignado
                    //System.out.println(tipo+" "+var+" "+valor);
                    aux = new objetoTabla(var,valor,tipo);
                }
                else{ //No tiene un valor asignado
                    aux = new objetoTabla(var,tipo);
                    //System.out.println(tipo+" "+var);
                }
                if(!error){
                    tablaSimbolos.add(aux);
                }
            }
            
            if(asignacion){
               for(objetoTabla o : tablaSimbolos){
                   if(var.equals(o.var)){
                       o.setValor(valor);
                       
                   }
               }
            }
            //Reset para la siguiente declaracion o asignación
            asignacion = error = valAsig = declaracion = fin  = false;
            tipo = null;
            }
        }
    }

    void imprimirTablaSimbolos(){
        System.out.println("---------TABLA DE SIMBOLOS-----------");
        System.out.println("Tipo - Variable - Valor ");
        
        for(objetoTabla o : tablaSimbolos ){
        System.out.println(o.tipo+" "+o.var+" "+o.valor);
        String[] tb  = {o.tipo.toString(),o.var,o.valor};
        tblModel.addRow(tb);
        }
    }
    
    void checarTablaSimbolos(String id){
        Boolean existe = false;
        for(objetoTabla o : tablaSimbolos ){
            if(id.equals(o.var)){
                if(declaracion){
                    //System.out.println("Error variable: "+tipo+" '"+id+"' ya asignada al tipo: "+o.tipo);
                    semant += "Error variable: "+tipo+" '"+id+"' ya asignada al tipo: "+o.tipo+"\n";
                    error = true;
                }
                else{
                //System.out.println("ASIGNANDO");
                }
            existe = true;
            break;
            }
        }
        if(asignacion&&!existe){
            //System.out.println("Error variable: '"+id+"' no ha sido creada");
            semant += "Error variable: '"+id+"' no ha sido creada\n";
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAbrirFile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAnalizado = new javax.swing.JTextArea();
        btnLimpiar = new javax.swing.JButton();
        labelTitulo = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAnalizar = new javax.swing.JTextArea();
        btnAnalizarSint = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAnaliSint = new javax.swing.JTextArea();
        btnAnalizarLex = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        semantico = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        btnAnalizarSint1 = new javax.swing.JButton();
        btnLimpiar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnAbrirFile.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnAbrirFile.setText("Abrir Archivo");
        btnAbrirFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirFileActionPerformed(evt);
            }
        });

        txtAnalizado.setColumns(20);
        txtAnalizado.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtAnalizado.setRows(5);
        jScrollPane1.setViewportView(txtAnalizado);

        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        labelTitulo.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        labelTitulo.setText("Analizador");

        txtAnalizar.setColumns(20);
        txtAnalizar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtAnalizar.setRows(5);
        jScrollPane2.setViewportView(txtAnalizar);

        btnAnalizarSint.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnAnalizarSint.setText("Analizar");
        btnAnalizarSint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarSintActionPerformed(evt);
            }
        });

        txtAnaliSint.setColumns(20);
        txtAnaliSint.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtAnaliSint.setRows(5);
        jScrollPane3.setViewportView(txtAnaliSint);

        btnAnalizarLex.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnAnalizarLex.setText("Analizar");
        btnAnalizarLex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarLexActionPerformed(evt);
            }
        });

        jLabel1.setText("Entrada de código:");

        jLabel2.setText("Lexico:");

        jLabel3.setText("Sintáctico");

        semantico.setColumns(20);
        semantico.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        semantico.setRows(5);
        jScrollPane4.setViewportView(semantico);

        jLabel4.setText("Semantico:");

        btnAnalizarSint1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnAnalizarSint1.setText("Analizar");
        btnAnalizarSint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarSint1ActionPerformed(evt);
            }
        });

        btnLimpiar1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLimpiar1.setText("Tabla de simbolos");
        btnLimpiar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTitulo)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnAnalizarSint)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(btnAnalizarLex, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnLimpiar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnAbrirFile, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                                                .addComponent(btnLimpiar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(155, 155, 155)
                                            .addComponent(jLabel4)))))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)))
                        .addComponent(btnAnalizarSint1)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelTitulo)
                        .addGap(48, 48, 48)
                        .addComponent(btnAbrirFile)
                        .addGap(18, 18, 18)
                        .addComponent(btnAnalizarLex)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiar)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiar1)
                        .addGap(56, 56, 56)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(btnAnalizarSint, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(btnAnalizarSint1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAbrirFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirFileActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File archivo = new File(chooser.getSelectedFile().getAbsolutePath());
        
        try {
            String st = new String(Files.readAllBytes(archivo.toPath()));
            txtAnalizar.setText(st);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(InterfazAnalizador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InterfazAnalizador.class.getName()).log(Level.SEVERE, null, ex);
        } catch(NullPointerException ex){
            Logger.getLogger(InterfazAnalizador.class.getName()).log(Level.SEVERE, null,ex);
        }
    }//GEN-LAST:event_btnAbrirFileActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        Limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnAnalizarSintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarSintActionPerformed
        // TODO add your handling code here:
        String st = txtAnalizar.getText();
        Sintax s = new Sintax(new proyecto.LexerCup(new StringReader(st)));
        try {
            s.parse();
            txtAnaliSint.setText("Analisis realizado correctamente");
            txtAnaliSint.setForeground(new Color(25,111,61));
        } catch (Exception ex) {
            Symbol sym = s.getS();
            txtAnaliSint.setText("Error de sintaxis, Linea: "+(sym.right+1)+" Columna: "+(sym.left+1)
            + " Texto: "+sym.value);
            txtAnaliSint.setForeground(Color.RED);
        }
    }//GEN-LAST:event_btnAnalizarSintActionPerformed

    private void btnAnalizarLexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarLexActionPerformed
        try {
            // TODO add your handling code here:
            analizarLex();
            imprimirTablaSimbolos();
        } catch (IOException ex) {
            Logger.getLogger(InterfazAnalizador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAnalizarLexActionPerformed

    private void btnAnalizarSint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarSint1ActionPerformed
    semantico.setText(semant);
    }//GEN-LAST:event_btnAnalizarSint1ActionPerformed

    private void btnLimpiar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiar1ActionPerformed
    jf.show();
    }//GEN-LAST:event_btnLimpiar1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazAnalizador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrirFile;
    private javax.swing.JButton btnAnalizarLex;
    private javax.swing.JButton btnAnalizarSint;
    private javax.swing.JButton btnAnalizarSint1;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnLimpiar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel labelTitulo;
    private javax.swing.JTextArea semantico;
    private javax.swing.JTextArea txtAnaliSint;
    private javax.swing.JTextArea txtAnalizado;
    private javax.swing.JTextArea txtAnalizar;
    // End of variables declaration//GEN-END:variables

    private void Limpiar() {
        txtAnalizar.setText("");
        txtAnalizado.setText("");
        txtAnaliSint.setText("");
        semantico.setText("");
        semant="";
        tablaSimbolos = new ArrayList<objetoTabla>();
    }
}