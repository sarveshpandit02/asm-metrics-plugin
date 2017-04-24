package edu.utdallas.plugin;

import edu.utdallas.MetricsManager;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.Textifier;

import java.util.Arrays;
import java.util.List;

public class MyPrinter extends Textifier {

    List<String> operators = Arrays.asList(("IADD,LADD,"
                    + "FADD,DADD,ISUB,LSUB,FSUB,DSUB,IMUL,LMUL,FMUL,DMUL,IDIV,LDIV,"
                    + "FDIV,DDIV,IREM,LREM,FREM,DREM,INEG,LNEG,FNEG,DNEG,ISHL,LSHL,"
                    + "ISHR,LSHR,IUSHR,LUSHR,IAND,LAND,IOR,LOR,IXOR,LXOR,IINC,IF_ICMPNE,IF_ICMPEQ,IF_ICMPNE,IF_ICMPLT,IF_ICMPGE,IF_ICMPGT,IF_ICMPLE").split(","));
    String currentClassMethod;
    String currentClass;


    public MyPrinter(final int api) {
        super(api);
    }

    public MyPrinter() {
        this(Opcodes.ASM5);
        if (getClass() != MyPrinter.class) {
            throw new IllegalStateException();
        }
    }

    @Deprecated
    @Override
    public void visitMethodInsn(final int opcode, final String owner,
            final String name, final String desc) {
        if (api >= Opcodes.ASM5) {
            super.visitMethodInsn(opcode, owner, name, desc);
            return;
        }
        doVisitMethodInsn(opcode, owner, name, desc,
                opcode == Opcodes.INVOKEINTERFACE);
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner,
            final String name, final String desc, final boolean itf) {
        if (api < Opcodes.ASM5) {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
            return;
        }
        doVisitMethodInsn(opcode, owner, name, desc, itf);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        MetricsManager.addResultMessage(owner+"."+name,currentClassMethod,"Variables Referenced:");
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        MetricsManager.addResultMessage(type, currentClassMethod,"Exceptions Referenced:");
        super.visitTryCatchBlock(start, end, handler, type);
    }

    private void doVisitMethodInsn(final int opcode, final String owner,
            final String name, final String desc, final boolean itf) {
        String ownerClass = owner.replaceAll("/",".");
        if(ownerClass.equals(currentClass)){
            MetricsManager.addResultMessage(currentClass+"."+name,currentClassMethod,"Number of local methods:");
        }else{
            MetricsManager.addResultMessage(currentClass+"."+name,currentClassMethod,"Number of external methods:");
        }

        if(OPCODES[opcode].equals("INVOKESTATIC")){
            MetricsManager.addResultMessage(ownerClass,currentClassMethod,"Class References:");
        }
    }

    @Override public void visitTypeInsn(int opcode, String type) {
        if(OPCODES[opcode].equals("NEW")){
            MetricsManager.addResultMessage(type.replaceAll("/","."),currentClassMethod,"Class References:");
            MetricsManager.addResultMessage(type.replaceAll("/","."),currentClassMethod, "Number of expressions:");
        }
        if(OPCODES[opcode].equals("INSTANCEOF")){
            MetricsManager.addResultMessage(String.valueOf(opcode),currentClassMethod, "Number of operators:");
        }
        if(OPCODES[opcode].equals("CHECKCAST")){
            MetricsManager.addResultMessage(String.valueOf(opcode),currentClassMethod, "Number of casts:");
        }
        super.visitTypeInsn(opcode, type);
    }

    @Override public void visitInsn(int opcode) {
        if(OPCODES[opcode].contains("STORE")){
            MetricsManager.addResultMessage(String.valueOf(opcode),currentClassMethod, "Number of expressions:");
            MetricsManager.addResultMessage(String.valueOf(opcode),currentClassMethod, "Number of operators:");
        }
        if(operators.contains(OPCODES[opcode])){
            MetricsManager.addResultMessage(String.valueOf(opcode),currentClassMethod, "Number of operators:");
        }
        super.visitInsn(opcode);
    }

    @Override
    public Textifier visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        currentClass = name.substring(0,name.lastIndexOf('.'));
        currentClassMethod = name;

        // Method Args
        MetricsManager.addResultMessage(String.valueOf(Type.getArgumentTypes(desc).length),name,"Arguments:");

        String modifiers = appendAccess(access & ~Opcodes.ACC_VOLATILE);
        MetricsManager.addResultMessage(modifiers,name,"Modifiers:");

        // Exceptions thrown
        if (exceptions != null && exceptions.length > 0) {
            MetricsManager.addResultMessage(String.valueOf(exceptions.length),name,"Exceptions Thrown:");
        }
        Textifier t = new MyPrinter();
        return t;
    }



    @Override
    public void visitLineNumber(int line, Label start) {
        MetricsManager.addResultMessage(String.valueOf(line), currentClassMethod,"Number of Statements:");
        super.visitLineNumber(line, start);
    }

    /**
     * Appends a string representation of the given access modifiers to
     * {@link #buf buf}.
     *
     * @param access
     *            some access modifiers.
     */
    private String appendAccess(final int access) {
        String result = "";
        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            result = "public";
            buf.append("public ");
        }
        if ((access & Opcodes.ACC_PRIVATE) != 0) {
            result = "private";
            buf.append("private ");
        }
        if ((access & Opcodes.ACC_PROTECTED) != 0) {
            result = "protected";
            buf.append("protected ");
        }
        if ((access & Opcodes.ACC_FINAL) != 0) {
            result = "final";
            buf.append("final ");
        }
        if ((access & Opcodes.ACC_STATIC) != 0) {
            result = "static";
            buf.append("static ");
        }
        if ((access & Opcodes.ACC_SYNCHRONIZED) != 0) {
            result = "synchronized";
            buf.append("synchronized ");
        }
        if ((access & Opcodes.ACC_VOLATILE) != 0) {
            result = "volatile";
            buf.append("volatile ");
        }
        if ((access & Opcodes.ACC_TRANSIENT) != 0) {
            result = "transient";
            buf.append("transient ");
        }
        if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            result = "abstract";
            buf.append("abstract ");
        }
        if ((access & Opcodes.ACC_STRICT) != 0) {
            result = "strictfp";
            buf.append("strictfp ");
        }
        if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
            result = "synthetic";
            buf.append("synthetic ");
        }
        if ((access & Opcodes.ACC_MANDATED) != 0) {
            result = "mandated";
            buf.append("mandated ");
        }
        if ((access & Opcodes.ACC_ENUM) != 0) {
            result = "enum";
            buf.append("enum ");
        }
        return result;
    }
}
