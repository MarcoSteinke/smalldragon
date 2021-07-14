#ifndef NAMESPACE
#define NAMESPACE

struct TokenList;
struct Namespace;

struct Namespace* makeNamespace(struct TokenList* tokens, char* ast_filename, char* name);

void ns_parse_methods(struct Namespace* res, struct TokenList* copy);
void ns_parse_structs(struct Namespace* res, struct TokenList* copy);

#endif
