package org.vanautrui.languages;

import org.vanautrui.languages.commandline.CompilerPhases;
import org.vanautrui.languages.commandline.DragonCompiler;
import org.vanautrui.languages.compiler.parsing.astnodes.nonterminal.upperscopes.AST_Whole_Program;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;

public final class TestUtils {

    public static AST_Whole_Program parse_for_test(final String sourceCode, final boolean debug) throws Exception {

        //Write to file
        final String filename = "Main.dg";
        if(debug){
            out.println("write to "+filename);
        }
        Files.writeString(Paths.get(filename),sourceCode);

        //invoke dragon-lexer
        DragonCompiler.invokeDragonLexer(Paths.get(filename).toFile(),debug);

        //we now have the token file. can delete the source file
        if(debug){
            out.println("delete "+filename);
        }
        Files.delete(Paths.get(filename));

        //invoke dragon-parser
        final File tokensFile = Paths.get("."+filename+".tokens").toFile();
        DragonCompiler.invokeDragonParser(tokensFile,debug);

        //we now have the .json file. can delete the .tokens file now
        if(debug){
            out.println("delete "+tokensFile.toPath());
        }
        Files.delete(tokensFile.toPath());

        final ArrayList<File> files=new ArrayList<>();
        files.add(Paths.get("."+filename+".json").toFile());

        final AST_Whole_Program ast = DragonCompiler.parseASTFromJSONFiles(files, debug);

        //TODO: we have the AST now. we can delete the .json file now

        return ast;
    }

    public static Process compileAndRunProgramForTesting(
            final String sourceCode,
            final String filename,
            final String[] args,
            final boolean debug
    ) throws Exception {
        //gets a dragon source code, compiles to vm code, calls dracovm, and starts the executable

        if(debug){
            out.println("TestUtils::compileAndRunProgramForTesting");
        }

        //should create the .dracovm files for it
        final List<Path> vmcodes = generateVMCodeFromDragonCode(sourceCode, debug);

        //should create the executable and run it
        final Process pr = compile_and_run_vm_codes(
                vmcodes,
                Paths.get(filename,args),
                args,
                debug
        );
        return pr;
    }


    public static Process compile_and_run_but_not_waitFor(
            final String dragon_source,
            final String filename_without_extension,
            final boolean debug
    ) throws Exception{
        final Path filename_without_extns = Paths.get(filename_without_extension);

        //generate the vm code
        final List<Path> paths = generateVMCodeFromDragonCode(dragon_source, false);

        //generate the executable
        generateFromVMCodeAndWriteExecutable(paths,filename_without_extns,debug);

        //run that executable
        final Process pr = Runtime.getRuntime().exec("./"+filename_without_extension);
        return pr;
    }


    private static List<Path> generateVMCodeFromDragonCode(final String source, final boolean debug) throws Exception{
        //generates vm codes from dragon codes, and writes them to files. returns paths to those files

        final String filename = "Main.dg";

        //write dragon code to file
        if(debug){
            out.println("write to "+filename);
        }
        Files.writeString(Paths.get(filename),source);

        //invoke dragon-lexer
        DragonCompiler.invokeDragonLexer(Paths.get(filename).toFile(),debug);

        final File tokensFile = Paths.get("."+filename+".tokens").toFile();

        //invoke dragon-parser
        DragonCompiler.invokeDragonParser(tokensFile,debug);

        final ArrayList<File> jsonFiles=new ArrayList<>();
        jsonFiles.add(Paths.get("."+filename+".json").toFile());

        final AST_Whole_Program ast = DragonCompiler.parseASTFromJSONFiles(jsonFiles,debug);
        //we are in debug mode since we are running tests

        return CompilerPhases.phase_vm_codegeneration(ast,false,debug);
    }

    private static void generateFromVMCodeAndWriteExecutable(
            final List<Path> vmcodes,
            final Path filename,
            final boolean debug
    ) throws Exception {
        //writes an executable with the name we requested

        //dracovm only accepts filenames as arguments

        DragonCompiler.invokeDracoVMCompiler(vmcodes,debug);


        //move our 'main' executable into the desired filename
        final String call = "mv main "+filename.toString();
        if(debug){
            out.println(call);
        }
        Runtime.getRuntime().exec(call).waitFor();
    }

    private static Process compile_and_run_vmcodes_but_not_waitFor(
            final List<Path> vmcode_paths,
            final Path filename_without_extension,
            final String[] args,
            final boolean debug
    ) throws Exception{

        generateFromVMCodeAndWriteExecutable(vmcode_paths,filename_without_extension,debug);

        final String call = "./"+filename_without_extension+" "+ String.join(" ", Arrays.asList(args));

        if(debug) {
            out.println(call);
        }

        Process pr = Runtime.getRuntime().exec(call);
        return pr;
    }

    private static Process compile_and_run_vm_codes(
            final List<Path> vmcode_paths,
            final Path filename_without_extension,
            final String[] args,
            final boolean debug
    ) throws Exception{

        final Process pr = compile_and_run_vmcodes_but_not_waitFor(
                vmcode_paths,
                filename_without_extension,
                args,
                debug
        );

        pr.waitFor();

        //for debugging purposes we can keep the artifacts to look at them later.
        final boolean deleteArtifacts = !debug;

        if(deleteArtifacts) {

            //delete all the .dracovm  files that have been created
            for (final Path dracovmfilepath : vmcode_paths) {
                if(debug) {
                    out.println("delete: " + dracovmfilepath);
                }
                Files.delete(dracovmfilepath);
            }

            //delete the executable
            if (debug) {
                out.println("delete: " + filename_without_extension);
            }
            Files.delete(filename_without_extension);

            //TODO: delete the assembly files of the subroutines

        }

        //TODO: there could be multiple assembly files, as the dracovm compiler becomes incremental
        //TODO: delete those aswell, as we are in a test environment

        return pr;
    }
}
