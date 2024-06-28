package recognizers.flex;

%%

%class RelationRecognizer

%int

RELATION_REGEX=[a-zA-Z._][a-zA-Z0-9._]*

%%

{RELATION_REGEX} {
    return 1;
}

. { }