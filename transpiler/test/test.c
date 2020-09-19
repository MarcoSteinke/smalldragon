#include <stdio.h>
#include <stdbool.h>
#include <inttypes.h>
#include <stdlib.h>

#include "../main/transpiler.h"
#include "../main/flags.h"

#include "test.h"
#include "teststatuscode.h"
#include "suite/test_op.h"
#include "suite/test_other.h"
#include "suite/test_assign.h"

int transpiler_test_all(bool debug){
	
	printf("Running tests for smalldragon/transpiler:\n");
	
	const uint16_t testsRun = 
		TEST_COUNT_OP 
		+ TEST_COUNT_OTHER
		+ TEST_COUNT_ASSIGN;
		
	uint16_t testsPassed 	= 0;
	
	bool (*tests[testsRun])(bool debug);
	
	//first the ones from test_op.h
	tests[0] = test_add;
	tests[1] = test_sub;
	tests[2] = test_mul;
	tests[3] = test_div;
	tests[4] = test_mod;
	tests[5] = test_precedence;
	tests[6] = test_or;
	tests[7] = test_and;
	tests[8] = test_not;
	tests[9] = test_greater;
	tests[10] = test_lesser;
	tests[11] = test_geq;
	tests[12] = test_leq;
	tests[13] = test_eq;
	tests[14] = test_neq;
	tests[15] = test_chained_cmp;
	tests[16] = test_bitwise_and;
	tests[17] = test_bitwise_or;
	tests[18] = test_bitwise_leftshift;
	tests[19] = test_bitwise_rightshift;
	tests[20] = test_bitwise_xor;
	tests[21] = test_bitwise_neg;
	
	// then the ones from test_other.h
	tests[22] = test_statuscode;
	tests[23] = test_simplevar;
	tests[24] = test_ifstmt;
	tests[25] = test_whilestmt;
	tests[26] = test_subrcall;
	tests[27] = test_recursive;
	tests[28] = test_charconst_cmp;
	tests[29] = test_break;
	
	//from test_assign.h
	tests[30]=test_assign;
	tests[31]=test_assign_plus;
	tests[32]=test_assign_minus;
	tests[33]=test_assign_times;
	tests[34]=test_assign_div;
	
	for(int i=0; i < testsRun; i++){
		testsPassed += (*tests[i])(debug);
		
		if(testsPassed != i+1){
			printf("\nTest Failure\n");
			break;
		}
	}
	
	printf("\nPassed %d of %d Tests\n",testsPassed,testsRun);
	
	if(testsPassed != testsRun){ return 1; }
	
	return 0;
}
