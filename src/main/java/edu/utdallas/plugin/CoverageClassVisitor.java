package edu.utdallas.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceMethodVisitor;

public class CoverageClassVisitor extends ClassVisitor implements Opcodes {
    String classname;


    public CoverageClassVisitor(final ClassVisitor cv) {
       super(ASM5, cv);
    }

    @Override
    public void visit(int i, int i1, String className, String s1, String s2, String[] strings) {
        super.visit(i, i1, className, s1, s2, strings);
        this.classname=className;

    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
                                     final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        MyPrinter p = new MyPrinter(Opcodes.ASM5);

        p.visitMethod(access,(classname+"/"+name).replaceAll("/","."),desc,signature,exceptions);

        //MethodVisitor mv1 = mv == null ? null : new CoverageMethodVisitor(mv,classname,name,exceptions);
        return new TraceMethodVisitor(mv, p);

        //return mv == null ? null : new CoverageMethodVisitor(mv,classname,name,exceptions);
    }




}
