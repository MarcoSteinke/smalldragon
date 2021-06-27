#include <string.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>

#include "SimpleVar.h"
#include "expr/Expr.h"

#include "ast/util/free_ast.h"

#include "token/list/TokenList.h"
#include "token/TokenKeys.h"
#include "token/token/token.h"

struct SimpleVar* makeSimpleVar(struct TokenList* tokens, bool debug) {

	if(debug){
		printf("SimpleVar(...) from ");
		list_print(tokens);
	}
	
	if(list_size(tokens) == 0){  return NULL;  }

	struct SimpleVar* res = make(SimpleVar);
	
	res->super.line_num    = list_head(tokens)->line_num;
	res->super.annotations = 0;

	res->count_indices = 0;
	res->indices = malloc(sizeof(struct Expr*)*1);

	struct TokenList* copy = list_copy(tokens);

	struct Token* token = list_head(copy);

	if (token->kind == ID) {
		strcpy(res->name, token->value_ptr);
		list_consume(copy,1);

		//it could have an index
		while (list_size(copy) > 0 
			&& list_head(copy)->kind == LBRACKET
		){

			if(!list_expect(copy, LBRACKET)){
				free(res);
				freeTokenListShallow(copy);
				return NULL;
			}
			res->indices[res->count_indices] = makeExpr(copy,debug);
			res->count_indices += 1;
			res->indices = 
				realloc(
					res->indices, 
					sizeof(struct Expr*) 
					* (res->count_indices+1)
				);
			
			if(res->indices[res->count_indices-1] == NULL){
				free(res);
				freeTokenListShallow(copy);
				return NULL;
			}

			if(!list_expect(copy, RBRACKET)){
				free(res);
				freeTokenListShallow(copy);
				return NULL;
			}

		}

	} else {
		
		free(res);
		freeTokenListShallow(copy);
		return NULL;
	}
	
	if(debug){
		printf("sucess parsing SimpleVar\n");
	}

	list_set(tokens,copy);
	freeTokenListShallow(copy);

	return res;
}
