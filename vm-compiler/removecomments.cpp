#include <iostream>
#include <fstream>
#include <string>
#include <vector>

#include "removecomments.hpp"

using namespace std;

vector<string> remove_comments(vector<string> lines){
	vector<string> result;
	
	for(int i=0;i<lines.size();i++){
		string s = lines.at(i);
		string s1;
		
		int pos=s.find("//");
		if(pos==string::npos){
			s1=s;
		}else{
			s1=s.substr(0,pos);
		}
		
		//cout << s1 << endl;
		
		if(!isempty(s1)){
			result.push_back(s1);
		}
	}
	
	return result;
}

bool isempty(string s){
	for(int i=0;i<s.length();i++){
		char c = s.at(i);
		if(c!='\t' && c!=' ' && c!='\n'){
			//cout << "not empty at " << c << endl;
			return false;
		}
	}
	return true;
}


