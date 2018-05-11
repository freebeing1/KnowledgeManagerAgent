/* Generated By:JavaCC: Do not edit this line. GLParserTokenManager.java */
package kr.ac.uos.ai.arbi.model.parser;
import java.util.*;
import java.io.*;
import kr.ac.uos.ai.arbi.model.*;

/** Token Manager. */
public class GLParserTokenManager implements GLParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x4000000L) != 0L)
            return 23;
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 34:
         return jjStopAtPos(0, 2);
      case 35:
         return jjMoveStringLiteralDfa1_0(0x20000000L);
      case 40:
         return jjStopAtPos(0, 27);
      case 41:
         return jjStopAtPos(0, 28);
      case 45:
         return jjMoveStringLiteralDfa1_0(0x4000000L);
      default :
         return jjMoveNfa_0(16, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 40:
         if ((active0 & 0x20000000L) != 0L)
            return jjStopAtPos(1, 29);
         break;
      case 45:
         return jjMoveStringLiteralDfa2_0(active0, 0x4000000L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 62:
         if ((active0 & 0x4000000L) != 0L)
            return jjStopAtPos(2, 26);
         break;
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 23;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 16:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 22)
                        kind = 22;
                     jjCheckNAddStates(0, 2);
                  }
                  else if ((0x100003600L & l) != 0L)
                  {
                     if (kind > 1)
                        kind = 1;
                     jjCheckNAdd(0);
                  }
                  else if ((0x280000000000L & l) != 0L)
                     jjCheckNAddStates(3, 5);
                  else if (curChar == 36)
                  {
                     if (kind > 25)
                        kind = 25;
                     jjCheckNAdd(19);
                  }
                  else if (curChar == 46)
                     jjstateSet[jjnewStateCnt++] = 2;
                  break;
               case 23:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(7, 8);
                  else if (curChar == 46)
                     jjstateSet[jjnewStateCnt++] = 2;
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 22)
                        kind = 22;
                     jjCheckNAdd(21);
                  }
                  break;
               case 0:
                  if ((0x100003600L & l) == 0L)
                     break;
                  kind = 1;
                  jjCheckNAdd(0);
                  break;
               case 1:
                  if (curChar == 46)
                     jjstateSet[jjnewStateCnt++] = 2;
                  break;
               case 2:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAddStates(6, 9);
                  break;
               case 4:
                  if ((0x280000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 5:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAddStates(10, 12);
                  break;
               case 6:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAddTwoStates(7, 1);
                  break;
               case 7:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(7, 8);
                  break;
               case 8:
                  if (curChar != 46)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAddStates(13, 16);
                  break;
               case 10:
                  if ((0x280000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 11:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAddStates(17, 19);
                  break;
               case 12:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAddStates(20, 24);
                  break;
               case 13:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAddStates(25, 30);
                  break;
               case 14:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAddStates(31, 35);
                  break;
               case 15:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAddStates(36, 41);
                  break;
               case 17:
                  if ((0x7ff200000000000L & l) == 0L)
                     break;
                  if (kind > 24)
                     kind = 24;
                  jjstateSet[jjnewStateCnt++] = 17;
                  break;
               case 18:
                  if (curChar != 36)
                     break;
                  kind = 25;
                  jjCheckNAdd(19);
                  break;
               case 19:
                  if ((0x3ff600000000000L & l) == 0L)
                     break;
                  if (kind > 25)
                     kind = 25;
                  jjCheckNAdd(19);
                  break;
               case 20:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAddStates(3, 5);
                  break;
               case 21:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 22)
                     kind = 22;
                  jjCheckNAdd(21);
                  break;
               case 22:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 22)
                     kind = 22;
                  jjCheckNAddStates(0, 2);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 16:
               case 17:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 24)
                     kind = 24;
                  jjCheckNAdd(17);
                  break;
               case 3:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(42, 43);
                  break;
               case 9:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(44, 45);
                  break;
               case 19:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 25)
                     kind = 25;
                  jjstateSet[jjnewStateCnt++] = 19;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 23 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private int jjMoveStringLiteralDfa0_1()
{
   switch(curChar)
   {
      case 34:
         return jjStopAtPos(0, 3);
      case 92:
         return jjMoveStringLiteralDfa1_1(0x1f0L);
      default :
         return 1;
   }
}
private int jjMoveStringLiteralDfa1_1(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      return 1;
   }
   switch(curChar)
   {
      case 34:
         if ((active0 & 0x10L) != 0L)
            return jjStopAtPos(1, 4);
         break;
      case 102:
         if ((active0 & 0x100L) != 0L)
            return jjStopAtPos(1, 8);
         break;
      case 110:
         if ((active0 & 0x40L) != 0L)
            return jjStopAtPos(1, 6);
         break;
      case 114:
         if ((active0 & 0x80L) != 0L)
            return jjStopAtPos(1, 7);
         break;
      case 116:
         if ((active0 & 0x20L) != 0L)
            return jjStopAtPos(1, 5);
         break;
      default :
         return 2;
   }
   return 2;
}
static final int[] jjnextStates = {
   21, 7, 8, 21, 7, 1, 1, 3, 6, 15, 1, 6, 14, 9, 1, 6, 
   13, 1, 6, 12, 7, 8, 1, 6, 12, 7, 8, 9, 1, 6, 13, 7, 
   8, 1, 6, 14, 7, 8, 1, 3, 6, 15, 4, 5, 10, 11, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, null, null, null, null, null, null, 
"\55\55\76", "\50", "\51", "\43\50", };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
   "IN_STRING",
};

/** Lex State array. */
public static final int[] jjnewLexState = {
   -1, -1, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, -1, -1, 
};
static final long[] jjtoToken = {
   0x3fc00009L, 
};
static final long[] jjtoSkip = {
   0x6L, 
};
static final long[] jjtoSpecial = {
   0x2L, 
};
static final long[] jjtoMore = {
   0x3f0L, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[23];
private final int[] jjstateSet = new int[46];
private final StringBuilder jjimage = new StringBuilder();
private StringBuilder image = jjimage;
private int jjimageLen;
private int lengthOfMatch;
protected char curChar;
/** Constructor. */
public GLParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/** Constructor. */
public GLParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 23; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
public void SwitchTo(int lexState)
{
   if (lexState >= 2 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind);
   t.kind = jjmatchedKind;
   t.image = curTokenImage;

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      matchedToken.specialToken = specialToken;
      return matchedToken;
   }
   image = jjimage;
   image.setLength(0);
   jjimageLen = 0;

   for (;;)
   {
     switch(curLexState)
     {
       case 0:
         jjmatchedKind = 0x7fffffff;
         jjmatchedPos = 0;
         curPos = jjMoveStringLiteralDfa0_0();
         break;
       case 1:
         jjmatchedKind = 0x7fffffff;
         jjmatchedPos = 0;
         curPos = jjMoveStringLiteralDfa0_1();
         if (jjmatchedPos == 0 && jjmatchedKind > 9)
         {
            jjmatchedKind = 9;
         }
         break;
     }
     if (jjmatchedKind != 0x7fffffff)
     {
        if (jjmatchedPos + 1 < curPos)
           input_stream.backup(curPos - jjmatchedPos - 1);
        if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
           matchedToken = jjFillToken();
           matchedToken.specialToken = specialToken;
           TokenLexicalActions(matchedToken);
       if (jjnewLexState[jjmatchedKind] != -1)
         curLexState = jjnewLexState[jjmatchedKind];
           return matchedToken;
        }
        else if ((jjtoSkip[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
           if ((jjtoSpecial[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
           {
              matchedToken = jjFillToken();
              if (specialToken == null)
                 specialToken = matchedToken;
              else
              {
                 matchedToken.specialToken = specialToken;
                 specialToken = (specialToken.next = matchedToken);
              }
           }
         if (jjnewLexState[jjmatchedKind] != -1)
           curLexState = jjnewLexState[jjmatchedKind];
           continue EOFLoop;
        }
        MoreLexicalActions();
      if (jjnewLexState[jjmatchedKind] != -1)
        curLexState = jjnewLexState[jjmatchedKind];
        curPos = 0;
        jjmatchedKind = 0x7fffffff;
        try {
           curChar = input_stream.readChar();
           continue;
        }
        catch (java.io.IOException e1) { }
     }
     int error_line = input_stream.getEndLine();
     int error_column = input_stream.getEndColumn();
     String error_after = null;
     boolean EOFSeen = false;
     try { input_stream.readChar(); input_stream.backup(1); }
     catch (java.io.IOException e1) {
        EOFSeen = true;
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
        if (curChar == '\n' || curChar == '\r') {
           error_line++;
           error_column = 0;
        }
        else
           error_column++;
     }
     if (!EOFSeen) {
        input_stream.backup(1);
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
     }
     throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
   }
  }
}

void MoreLexicalActions()
{
   jjimageLen += (lengthOfMatch = jjmatchedPos + 1);
   switch(jjmatchedKind)
   {
      case 4 :
         image.append(input_stream.GetSuffix(jjimageLen));
         jjimageLen = 0;
                          image.setLength(image.length() - 2); image.append("\u005c"");
         break;
      case 5 :
         image.append(input_stream.GetSuffix(jjimageLen));
         jjimageLen = 0;
                          image.setLength(image.length() - 2); image.append("\u005ct");
         break;
      case 6 :
         image.append(input_stream.GetSuffix(jjimageLen));
         jjimageLen = 0;
                          image.setLength(image.length() - 2); image.append("\u005cn");
         break;
      case 7 :
         image.append(input_stream.GetSuffix(jjimageLen));
         jjimageLen = 0;
                          image.setLength(image.length() - 2); image.append("\u005cr");
         break;
      case 8 :
         image.append(input_stream.GetSuffix(jjimageLen));
         jjimageLen = 0;
                          image.setLength(image.length() - 2); image.append("\u005cf");
         break;
      default :
         break;
   }
}
void TokenLexicalActions(Token matchedToken)
{
   switch(jjmatchedKind)
   {
      case 3 :
        image.append(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1)));
                image.setLength(image.length() - 1);
                matchedToken.image = image.toString();
         break;
      default :
         break;
   }
}
private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}
