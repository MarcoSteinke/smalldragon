#include <assert.h>
#include <string.h>
#include <stdlib.h>

//AST Includes
#include "ast/ast.h"
#include "ast/util/str_ast.h"
#include "ast/util/equals_ast.h"

//Table Includes
#include "tables/sst/sst.h"
#include "tables/symtable/symtable.h"

//Typeinference Includes
#include "transpiler/main/typeinference/typeinfer.h"

//Typechecker Includes
#include "typecheck_errors.h"
#include "typecheck_utils.h"
#include "typecheck_stmts.h"
#include "typecheck.h"
#include "tcctx.h"

void tc_stmt(struct Stmt* s, struct TCCtx* tcctx){

	tcctx->current_line_num = s->super.line_num;

	switch(s->kind){
	
		case 0: tc_loopstmt(s->ptr.m0,   tcctx); break;
		case 1: tc_methodcall(s->ptr.m1, tcctx); break;
		case 2: tc_whilestmt(s->ptr.m2,  tcctx); break;
		case 3: tc_ifstmt(s->ptr.m3,     tcctx); break;
		case 4: tc_retstmt(s->ptr.m4,    tcctx); break;
		case 5: tc_assignstmt(s->ptr.m5, tcctx); break;
		
		case 7: tc_forstmt(s->ptr.m7,    tcctx); break;
		case 8: tc_switchstmt(s->ptr.m8, tcctx); break;
	}
}

void tc_assignstmt(struct AssignStmt* a, struct TCCtx* tcctx){

	tcctx->current_line_num = a->super.line_num;

	//we make an exception
	//TODO: only make exception for array types
	//and other applicable types
	if(is_malloc(a->expr))
		{ return; }

	struct Type* rightType = 
		infer_type_expr(tcctx->st, a->expr);
	
	struct Type* leftType = a->optType;
	
	if(a->optType == NULL){
		leftType  = infer_type_variable(tcctx->st, a->var);
	}
	
	if(is_integer_type(leftType) 
	&& is_integer_type(rightType))
		{ return; }
	
	if(!eq_type(leftType, rightType)){
	
		char* str_t1 = strType(leftType);
		char* str_t2 = strType(rightType);
		
		char* str_a  = strAssignStmt(a);
		
		char msg[200];
		
		sprintf(msg, "\t%s\nexpected type: %s, actual type: %s\n", str_a, str_t1, str_t2);
		strcat(msg, ERR_ASSIGN_TYPES_MISMATCH);
		
		free(str_t1);
		free(str_t2);
		free(str_a);
	
		error(tcctx, msg);
	}
}

void tc_methodcall(struct Call* m, struct TCCtx* tcctx){

	tcctx->current_line_num = m->super.line_num;

	struct SSTLine* line = sst_get(tcctx->st->sst, m->name);
	
	if(line->method == NULL){
		
		if(line->isLibC){
			//we do not have the AST for libC 
			//subroutines, so we cannot typecheck the call
			return;
		}
		
		
		char msg[150];
		sprintf(msg, "SUBR NOT FOUND IN SST: %s\n", m->name);
		strcat(msg, ERR_SUBR_NOT_FOUND);
		error(tcctx, msg);
	}
	assert(line->method != NULL);
	
	struct Method* method = line->method;
	
	const uint8_t expect_args = method->count_args;
	const uint8_t actual_args = m->count_args;
	
	if(actual_args != expect_args){
		
		char* s1 = strCall(m);
		
		char msg[200];
		sprintf(msg, "\t%s\nexpected: %d args\n", s1, expect_args);
		strcat(msg, ERR_NUM_ARGS);
		
		free(s1);
		
		error(tcctx, msg);
	}
	
	for(uint8_t i = 0; i < expect_args; i++){
	
		struct Type* expect_type = method->args[i]->type;
		
		struct Type* actual_type = 
			infer_type_expr(tcctx->st, m->args[i]);
			
		if(is_integer_type(expect_type) 
		&& is_integer_type(actual_type))
			{ continue; }
		
		if(!eq_type(expect_type, actual_type)){
			
			char* s1 = strCall(m);
			char* s2 = strExpr(m->args[i]);
			
			char* sTypeActual   = strType(actual_type);
			char* sTypeExpected = strType(expect_type);
			
			char msg[200];
			sprintf(msg, "\t%s\n%s, (of type %s), but expected type %s\n", s1, s2, sTypeActual, sTypeExpected);
			strcat(msg, ERR_ARG_TYPES);
			
			free(s1);
			free(s2);
			free(sTypeActual);
			free(sTypeExpected);
			
			error(tcctx, msg);
		}
	}
}

void tc_ifstmt(struct IfStmt* i, struct TCCtx* tcctx){
	
	tcctx->current_line_num = i->super.line_num;
	
	struct Type* type = 
		infer_type_expr(tcctx->st, i->condition);
	
	if(!is_bool_type(type)){
		
		char* s1 = strExpr(i->condition);
		
		char msg[100];
		sprintf(msg, "\t%s\n", s1);
		strcat(msg, ERR_CONDITION_REQUIRES_BOOL);
		
		free(s1);
		
		error(tcctx, msg);
	}
}

void tc_whilestmt(struct WhileStmt* w, struct TCCtx* tcctx){

	tcctx->current_line_num = w->super.line_num;

	struct Type* type = 
		infer_type_expr(tcctx->st, w->condition);
	
	if(!is_bool_type(type)){
		
		char* s1 = strExpr(w->condition);
		
		char msg[200];
		sprintf(msg, "\t%s\n", s1);
		strcat(msg, ERR_CONDITION_REQUIRES_BOOL);
		
		free(s1);
		
		error(tcctx, msg);
	}
}

void tc_loopstmt(struct LoopStmt* l, struct TCCtx* tcctx){

	tcctx->current_line_num = l->super.line_num;

	struct Type* type = 
		infer_type_expr(tcctx->st, l->count);
	
	if(!is_integer_type(type)){
		
		char* s1 = strExpr(l->count);
		
		char msg[200];
		sprintf(msg, "\tloop %s\n", s1);
		strcat(msg, ERR_LOOP_REQUIRES_INT);
		
		free(s1);
		
		error(tcctx, msg);
	}
}

void tc_retstmt(struct RetStmt* r, struct TCCtx* tcctx){

	tcctx->current_line_num = r->super.line_num;

	struct Type* returnType = tcctx->currentFn->returnType;
	
	struct Type* returnedType = 
		infer_type_expr(tcctx->st, r->returnValue);
	
	if(is_integer_type(returnType) 
	&& is_integer_type(returnedType))
		{ return; }
	
	if(!eq_type(returnType, returnedType)){
		
		char* s1 = strType(returnType);
		char* s2 = strType(returnedType);
		char* s3 = strRetStmt(r);
		
		char msg[200];
		sprintf(msg, "\t%s\nexpected type: %s, actual type: %s\n", s3, s1, s2);
		strcat(msg, ERR_RETURN_TYPE_MISMATCH);

		free(s1);
		free(s2);
		free(s3);
		
		error(tcctx, msg);
	}
}

void tc_switchstmt(struct SwitchStmt* s, struct TCCtx* tcctx){

	tcctx->current_line_num = s->super.line_num;

	struct Type* type = 
		infer_type_expr(tcctx->st, s->expr);
	
	if(!is_primitive_type(type)){
		
		char* s1 = strExpr(s->expr);
		
		char msg[200];
		sprintf(msg, "\tswitch %s\n", s1);
		strcat(msg, ERR_SWITCH_REQUIRES_PRIMITIVE_TYPE);
		
		free(s1);
	
		error(tcctx, msg);
	}
	
	for(uint16_t i = 0; i < s->count_cases; i++){
	
		struct CaseStmt* c = s->cases[i];
		
		const bool isBool = c->kind == 0;
		const bool isChar = c->kind == 1;
		const bool isInt  = c->kind == 2;
		
		bool isErr = false;
		
		if(isBool && !is_bool_type(type)){ isErr = true; }
	
		if(isChar && !is_char_type(type)){ isErr = true; }
		
		if(isInt && !is_integer_type(type)){ isErr = true; }
		
		if(isErr){
			
			char* s1 = strCaseStmt(c);
			
			char msg[200];
			sprintf(msg, "\t%s\n", s1);
			strcat(msg, ERR_CASE_TYPE_MISMATCH);
			
			free(s1);
			
			error(tcctx, msg);
		}
	}
}

void tc_forstmt(struct ForStmt* f, struct TCCtx* tcctx){
	
	tcctx->current_line_num = f->super.line_num;
	
	tc_range(f->range, tcctx);
	tc_stmtblock(f->block, tcctx);
}
