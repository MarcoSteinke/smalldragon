package org.vanautrui.languages;

import org.fusesource.jansi.Ansi;
import org.vanautrui.languages.commandline.dragonc;
import org.vanautrui.languages.commandline.dragoni;

import java.util.Arrays;

import static org.fusesource.jansi.Ansi.ansi;

public class App
{

    //public static final String lang_name="Dragon ";
    public static final String lang_name = "DRAGON ";

    public static void main( String[] args )
    {
        //this is the program that is actually intended to be used on the terminal:
        //for the compile part,

        //compile the source files given as arguments to java bytecode (for now)
        // dragon -c file1 file2 ...

        //compile all source files in that directory recursively
        // dragon -c directory1


        // dragon -i file1 file2 ... : interpret the given files and execute


        // dragon -i                 : just start an interpreter



        if(args.length<1){
            DragonTerminalUtil.printlnRed("Dragon Programming Language: https://github.com/pointbazaar/dragon");
            DragonTerminalUtil.printlnRed("use -i or -c argument");
            DragonTerminalUtil.printlnRed("-i starts the Interpreter");
            DragonTerminalUtil.printlnRed("-c starts the Compiler");
            DragonTerminalUtil.printlnRed("");
            DragonTerminalUtil.printlnRed("Usage: ");

            //TODO, it does not actually do that right now
            DragonTerminalUtil.printlnRed("dragon -c file1.dragon \tcompiles file1.dragon to file1.class");
            DragonTerminalUtil.printlnRed("dragon -i file1.dragon \tinterprets file1.dragon and prints the output to the console");
            
            System.exit(1);
        }else{
            String flag = args[0];

            switch (flag){
                case "-c":
                    DragonTerminalUtil.printlnRed(lang_name+"Compiler started",System.out);

                    dragonc.compile_main(Arrays.copyOfRange(args, 1, args.length));
                    break;
                case "-i":

                    try {

                        dragoni dragon_interpreter=new dragoni();
                        dragon_interpreter.interpret_main(Arrays.copyOfRange(args, 1, args.length));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    DragonTerminalUtil.printlnRed("first flag has to be -i or -c, not "+args[0],System.out);
                    System.exit(1);
                    break;
            }
        }
    }
}
