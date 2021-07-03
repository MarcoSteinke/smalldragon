#ifndef TYPE
#define TYPE

#include <stdbool.h>

struct TokenList;
struct Type;
struct BasicType;
struct TypeParam;
struct ArrayType;

struct Type* makeType2(struct TokenList* tokens, bool debug);
struct Type* makeType_1(struct BasicType* typeNode);
struct Type* makeType_2(struct TypeParam* typeNode);
struct Type* makeType_3(struct ArrayType* typeNode);

#endif
