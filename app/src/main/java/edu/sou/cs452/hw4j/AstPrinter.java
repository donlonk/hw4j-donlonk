/**
 * AstPrinter.java
 * This file is my version of the AST Printer for HW3J.
 * Prints the Little Pony AST in DOT format.
 * 
 * The dot string building is mostly AI generated code because it is annoying to write by hand,
 * and I don't much care how well you understand the DOT format.
 * 
 * @author Daniel DeFreez
 * @version Spring 2024
 */

 package edu.sou.cs452.hw4j;

 class AstPrinter implements Expr.Visitor<String> {
     // To ensure unique node IDs in the DOT output.
     private int nodeId = 0;
 
     String print(Expr expr) {
         return expr.accept(this);
     }
 
     @Override
     public String visitActorExpr(Expr.Actor expr) {
         StringBuilder builder = new StringBuilder();
         int actorId = nodeId++;
         builder.append("Actor_").append(actorId).append(" [label=\"Actor: ").append(expr.name.lexeme).append("\"];\n");
 
         for (Expr bodyExpr : expr.body) {
             String result = bodyExpr.accept(this);
             builder.append(result);
             String childId = extractFirstNodeId(result);
             builder.append("Actor_").append(actorId).append(" -> ").append(childId).append("\n");
         }
 
         return builder.toString();
     }
 
     @Override
     public String visitMethodCallExpr(Expr.MethodCall expr) {
         StringBuilder builder = new StringBuilder();
         int methodCallId = nodeId++;
 
         String methodName = expr.name.lexeme;
 
         builder.append("MethodCall_").append(methodCallId).append(" [label=\"call: ").append(methodName)
                 .append("\"];\n");
 
         for (Expr argument : expr.arguments) {
             String result = argument.accept(this);
             builder.append(result);
             String childId = extractFirstNodeId(result);
             builder.append("MethodCall_").append(methodCallId).append(" -> ").append(childId).append("\n");
         }
 
         // Connect this method call (the print node created above) to the object it is
         // called on.
         Expr obj = expr.obj;
         String objResult = obj.accept(this);
         builder.append(objResult);
         builder.append("MethodCall_").append(methodCallId).append(" -> ")
                 .append(extractFirstNodeId(objResult)).append("\n");
 
         return builder.toString();
     }
 
     @Override
     public String visitLiteralExpr(Expr.Literal expr) {
         int currentId = nodeId++;
         if (expr.value == null)
             return "nil_" + currentId + " [label=\"nil\"]";
 
         return "String_" + currentId + " [label=\""
                 + escapeString(expr.value.toString()) + "\"]\n";
     }
 
     @Override
     public String visitMemberAccessExpr(Expr.MemberAccess expr) {
         StringBuilder builder = new StringBuilder();
 
         String objResult = expr.obj.accept(this);
 
         int memberId = nodeId++;
 
         builder.append("MemberAccess_").append(memberId).append(" [label=\"MemberAccess: ")
                 .append(escapeString(expr.name.lexeme)).append("\"];\n");
 
         builder.append("MemberAccess_").append(memberId).append(" -> ")
                 .append(extractFirstNodeId(objResult)).append("\n");
 
         builder.append(objResult);
 
         return builder.toString();
     }
 
     @Override
     public String visitProgramExpr(Expr.Program expr) {
         StringBuilder builder = new StringBuilder();
         builder.append("digraph Program {\n");
         for (Expr e : expr.expressions) {
             String result = e.accept(this);
             // No need to append connections here since they are handled in individual
             // visits.
             builder.append(result).append("\n");
         }
         builder.append("}\n");
         return builder.toString();
     }
 
     @Override
     public String visitVariableExpr(Expr.Variable expr) {
         int variableId = nodeId++;
 
         // Assuming the variable name is stored directly as a string.
         return "Variable_" + variableId + " [label=\"Variable: " + escapeString(expr.name.lexeme) + "\"]\n";
     }
 
     @Override
     public String visitMethodDeclarationExpr(Expr.MethodDeclaration expr) {
         StringBuilder builder = new StringBuilder();
         int methodDeclId = nodeId++;
 
         // Label for the method declaration itself.
         builder.append("MethodDecl_").append(methodDeclId)
                 .append(" [label=\"Method: ").append(expr.name.lexeme)
                 .append("(");
 
         // Append parameters to label.
         for (int i = 0; i < expr.params.size(); i++) {
             Token param = expr.params.get(i);
             if (i > 0) {
                 builder.append(", ");
             }
             builder.append(param.lexeme);
         }
         builder.append(")\"];\n");
 
         // Connect method declaration to its body expressions.
         for (Expr bodyExp : expr.body) {
             String result = bodyExp.accept(this);
             builder.append(result);
             String childID = extractFirstNodeId(result);
             builder.append("MethodDecl_").append(methodDeclId).append(" -> ").append(childID).append("\n");
         }
 
         return "MethodDecl_" + methodDeclId;
     }
 
     private String escapeString(String value) {
         // Implement escaping of special characters here if necessary.
         return value.replace("\"", "\\\"");
     }
 
     /**
      * Helper method to extract the first node ID from a given string,
      * assuming the string starts with a node definition like "NodeName_NodeID".
      */
     private String extractFirstNodeId(String str) {
         int underscoreIndex = str.indexOf("_");
         int spaceIndex = str.indexOf(" ", underscoreIndex); // Find end of node id or label start.
         spaceIndex = spaceIndex == -1 ? str.length() : spaceIndex; // Handle case where no space is found after ID.
 
         return str.substring(0, spaceIndex); // Return substring containing just"NodeName_NodeID".
     }
 }
 