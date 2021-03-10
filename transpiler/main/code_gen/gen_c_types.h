#ifndef GENCTYPES
#define GENCTYPES

#include "ast/ast.h"

struct Ctx;

char* simpleType2CType(struct SimpleType* simpleType);

char* structType2CType(struct StructType* s);

char* primitiveType2CType(struct PrimitiveType* p);

char* type2CType(struct Type* type, struct Ctx* ctx);

char* arrayType2CType(struct ArrayType* arrType, struct Ctx* ctx);

char* subrType2CType(struct SubrType* subrType, struct Ctx* ctx);

char* typeParam2CType(struct TypeParam* typeParam, struct Ctx* ctx);

char* basicTypeWrapped2CType(struct BasicTypeWrapped* btw, struct Ctx* ctx);
//----------------------------
bool isIntType(struct Type* t);
//----------------------------
char* typeNameToCFormatStr(char* typeName);
#endif
