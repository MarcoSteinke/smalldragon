#include <string.h>
#include <stdio.h>
#include <stdlib.h>

#include "Namespace.h"
#include "subr/Method.h"
#include "struct/StructDecl.h"

#include "ast/util/free_ast.h"

#include "token/list/TokenList.h"
#include "token/TokenKeys.h"
#include "token/token/token.h"
#include "ExternC.h"

static void ns_parse_externc(struct Namespace* res, struct TokenList* copy);
static void ns_parse_methods(struct Namespace* res, struct TokenList* copy);
static void ns_parse_structs(struct Namespace* res, struct TokenList* copy);

void ns_parse_passthrough_includes(struct Namespace* p_namespace, struct TokenList* p_list);

struct Namespace* makeNamespace(struct TokenList* tokens, char* ast_filename, char* name) {

	struct Namespace* res = make(Namespace);

	//valgrind will complain about uninitialized bytes otherwise
	memset(res, 0, sizeof(struct Namespace));

	res->count_externc = 0;
	res->count_methods = 0;
	res->count_structs = 0;

	const uint16_t INITIAL_CAPACITY = 5;
	res->capacity_externc = INITIAL_CAPACITY;
	res->capacity_methods = INITIAL_CAPACITY;
	res->capacity_structs = INITIAL_CAPACITY;

	res->methods = malloc(sizeof(struct Method*)     * res->capacity_methods);
	res->structs = malloc(sizeof(struct StructDecl*) * res->capacity_structs);
	res->externc = malloc(sizeof(struct ExternC*) 	  * res->capacity_externc);

	res->src_path   = malloc(sizeof(char)*(strlen(name)+3+1));
	res->token_path = malloc(sizeof(char)*(strlen(tokens->rel_path) + 1));
	res->ast_path   = malloc(sizeof(char)*(strlen(ast_filename)+1));

	sprintf(res->src_path, "%s.dg", name);
	strcpy (res->token_path, tokens->rel_path);
	strcpy (res->ast_path,   ast_filename);
	strncpy(res->name,       name,      DEFAULT_STR_SIZE);
	
	struct TokenList* copy = list_copy(tokens);

	ns_parse_passthrough_includes(res, copy);
	ns_parse_externc(res, copy);
	ns_parse_structs(res, copy);
	ns_parse_methods(res, copy);

	list_set(tokens, copy);
	freeTokenListShallow(copy);

	return res;
}

void ns_parse_passthrough_includes(struct Namespace* res, struct TokenList* copy) {

	if (list_size(copy) == 0) { return; }

	struct Token* next = list_head(copy);

	//speculate that it won't be more than 100
	uint8_t includes_cap = 100;
	res->includes = malloc(sizeof(void*)*includes_cap);
	res->count_includes = 0;

	while (next->kind == INCLUDE_DECL) {
		char* string = malloc(strlen(next->value_ptr)+1);
		strcpy(string, next->value_ptr);

		res->includes[res->count_includes++] = string;
		list_consume(copy, 1);

		next = list_head(copy);
	}
}

static void ns_parse_externc(struct Namespace* res, struct TokenList* copy){

	if (list_size(copy) == 0) { return; }

	struct Token* next = list_head(copy);

	while (next->kind == EXTERNC) {

		struct ExternC* ec = makeExternC(copy);

		if(ec == NULL){
			printf("parsing error, expected an externc, but got:\n");
			list_print(copy);
			free_namespace(res);
			exit(1);
		}

		res->externc[res->count_externc] = ec;
		res->count_externc++;

		if(res->count_externc >= res->capacity_externc){
			res->capacity_externc *= 2;
			res->externc = realloc(res->externc,sizeof(void*)*(res->capacity_externc));
		}

		if(list_size(copy) == 0){ return; }

		next = list_head(copy);
	}
}

static void ns_parse_methods(struct Namespace* res, struct TokenList* copy) {
	
	if (list_size(copy) == 0) { return; }

	struct Token* next = list_head_without_annotations(copy);

	while (next->kind == FN) {
		struct Method* m = makeMethod(copy);
		if(m == NULL){
			printf("parsing error, expected a method, but got:\n");
			list_print(copy);

			free_namespace(res);
			exit(1);
		}

		res->methods[res->count_methods] = m;
		res->count_methods++;
		
		if(res->count_methods >= res->capacity_methods){
			res->capacity_methods *= 2;
			res->methods = realloc(res->methods,sizeof(struct Method*)*(res->capacity_methods));
		}

		if (list_size(copy) > 0) {
			next = list_head_without_annotations(copy);
		} else {
			break;
		}
	}

}
static void ns_parse_structs(struct Namespace* res, struct TokenList* copy) {
	
	if(list_size(copy) == 0) { return; }

	struct Token* next = list_head_without_annotations(copy);

	while (next->kind == STRUCT) {
		
		struct StructDecl* sd = makeStructDecl(copy);
		if(sd == NULL){
			printf("parsing error, expected a struct, but got %s\n", list_code(copy));
			exit(1);
		}

		res->structs[res->count_structs] = sd;
		res->count_structs++;
		
		res->structs = realloc(res->structs,sizeof(struct StructDecl*)*(res->count_structs+1));

		if (list_size(copy) > 0) {
			next = list_head_without_annotations(copy);
		} else {
			break;
		}
	}
}


