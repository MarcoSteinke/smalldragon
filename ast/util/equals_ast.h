#ifndef EQUALS_AST_H
#define EQUALS_AST_H

#include <stdbool.h>

/* the functions in this file are currently implemented
 * on an as-needed basis
 */
 
//NOTE: all these subroutines accept NULL
//as an argument

bool eq_type(struct Type* a, struct Type* b);

bool eq_subrtype(struct SubrType* a, struct SubrType* b);

bool eq_typeparam(struct TypeParam* a, struct TypeParam* b);

bool eq_simpletype(struct SimpleType* a, struct SimpleType* b);

bool eq_structtype(struct StructType* a, struct StructType* b);

bool eq_primitivetype(struct PrimitiveType* a, struct PrimitiveType* b);

bool eq_basictypewrapped(struct BasicTypeWrapped* a, struct BasicTypeWrapped* b);

bool eq_arraytype(struct ArrayType* a, struct ArrayType* b);

#endif
