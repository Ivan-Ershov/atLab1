package recognizers.flex;

%%

%class LexRecognizer

%int

RELATION_REGEX=[a-zA-Z._][a-zA-Z0-9._]*
CREATE=[cC][rR][eE][aA][tT][eE]
JOIN=[jJ][oO][iI][nN]
WHITESPACE=[ \t]

%%

{WHITESPACE}*{CREATE}{WHITESPACE}+({RELATION_REGEX}){WHITESPACE}*\({WHITESPACE}*(({RELATION_REGEX}){WHITESPACE}*({WHITESPACE}*,{WHITESPACE}*({RELATION_REGEX}))*){WHITESPACE}*\){WHITESPACE}*/\n {
    return 1;
}

{WHITESPACE}*({RELATION_REGEX}){WHITESPACE}*({WHITESPACE}{JOIN}{WHITESPACE}+({RELATION_REGEX}){WHITESPACE}*)?/\n {
    return 2;
}

\n { }

. {return 3;}