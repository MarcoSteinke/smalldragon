#include <stdio.h>
#include <stdbool.h>
#include <inttypes.h>

#include "test.h"

bool test_statuscode(bool debug);

void test_all(bool debug){
	
	printf("running tests for smalldragon/transpiler:\n");
	uint16_t testsRun    = 0;
	uint16_t testsPassed = 0;
	
	const uint16_t num_tests_max = 1;
	
	bool (*tests[num_tests_max]) (bool debug);
	
	tests[0] = test_statuscode;
	
	for(int i=0;i < num_tests_max; i++){
		testsPassed += tests[i](debug);
		testsRun++;
		if(testsPassed < testsRun){
			printf("last test did not pass!\n");
			if(!debug){
				printf("Isolating the failing Test:\n");
				printf("------------------------------\n");
				
				tests[i](true);
			}
			break;
		}
	}
	
	printf("passed %d of %d(this run) of %d(total test suite) \n",testsPassed,testsRun, num_tests_max);
}


// TESTS START

bool test_statuscode(bool debug){
	
	return true;
}
