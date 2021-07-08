# Variables
CXX=g++
CFLAGS=-Wall -Wsign-compare -pedantic -std=c++14 -g -fsanitize=address

TARGET=wrzecond
SOURCES=$(wildcard *.cpp)

MKDIR=mkdir -p
TMP_DIR=bin
DOC_DIR=doc

# Commands

.PHONY: all
all: compile doc

.PHONY: doc
doc:
	doxygen Doxyfile

.PHONY: run
run: compile
	./$(TARGET)

.PHONY: compile
compile: $(TARGET)

.PHONY: clean
clean:
	rm -rf $(TMP_DIR)/ $(DOC_DIR)/ $(TARGET)

# Generating main binary file and help .o files

$(TARGET):
	$(MKDIR) $(TMP_DIR)
	$(CXX) $(CFLAGS) $^ -o $@

$(TMP_DIR)/%.o: $(SOURCES)
	$(MKDIR) $(TMP_DIR)
	$(CXX) $(CFLAGS) $< -c -o $@

# Dependencies
$(TARGET): $(TMP_DIR)/main.o $(TMP_DIR)/CCommandLoader.o $(TMP_DIR)/CSetCommand.o $(TMP_DIR)/CSaveCommand.o $(TMP_DIR)/CPrintCommand.o $(TMP_DIR)/CLoadCommand.o $(TMP_DIR)/CGetCommand.o $(TMP_DIR)/CExportCommand.o $(TMP_DIR)/CClearCommand.o $(TMP_DIR)/CCommand.o $(TMP_DIR)/CCellName.o $(TMP_DIR)/CExpressionCell.o $(TMP_DIR)/CSpreadSheet.o $(TMP_DIR)/CLogicParser.o $(TMP_DIR)/CMathParser.o
$(TMP_DIR)/main.o: src/main.cpp src/CSpreadSheet.hpp src/CCommandLoader.hpp
$(TMP_DIR)/CSpreadSheet.o: src/CSpreadSheet.cpp src/CSpreadSheet.hpp src/cell/CCell.hpp

$(TMP_DIR)/CCommandLoader.o: src/CCommandLoader.cpp src/CCommandLoader.hpp src/CSpreadSheet.hpp src/command/CClearCommand.hpp src/command/CExportCommand.hpp src/command/CGetCommand.hpp src/command/CLoadCommand.hpp src/command/CPrintCommand.hpp src/command/CSaveCommand.hpp src/command/CSetCommand.hpp src/command/CCommand.hpp
$(TMP_DIR)/CLogicParser.o: src/parser/logic/CLogicParser.cpp src/parser/logic/CLogicParser.hpp src/CSpreadSheet.hpp src/parser/CParser.hpp src/parser/exceptions.hpp src/parser/logic/comp/CBinaryComparator.hpp src/parser/CBinarySymbol.hpp src/parser/logic/comp/CLessComparator.hpp src/parser/logic/comp/CGreaterComparator.hpp src/parser/logic/comp/CEqualComparator.hpp src/parser/logic/comp/CNotEqualComparator.hpp
$(TMP_DIR)/CMathParser.o: src/parser/math/CMathParser.cpp src/parser/math/CMathParser.hpp src/CSpreadSheet.hpp src/parser/CParser.hpp src/parser/exceptions.hpp src/parser/math/binary/CBinaryOperator.hpp src/parser/math/binary/op/CPlusOperator.hpp src/parser/math/binary/op/CMinusOperator.hpp src/parser/math/binary/op/CTimesOperator.hpp src/parser/math/binary/op/CDivideOperator.hpp src/parser/math/binary/CBinaryFunction.hpp src/parser/math/binary/fun/CMinFunction.hpp src/parser/math/binary/fun/CMaxFunction.hpp src/parser/math/binary/fun/CPowFunction.hpp src/parser/math/unary/CUnaryFunction.hpp src/parser/math/unary/CAbsFunction.hpp src/parser/math/unary/CSinFunction.hpp src/parser/math/unary/CCosFunction.hpp src/parser/CBinarySymbol.hpp

$(TMP_DIR)/CCellName.o: src/cell/CCellName.cpp src/cell/CCellName.hpp src/parser/exceptions.hpp
$(TMP_DIR)/CExpressionCell.o: src/cell/CExpressionCell.cpp src/cell/CExpressionCell.hpp src/CSpreadSheet.hpp src/parser/logic/CLogicParser.hpp src/parser/math/CMathParser.hpp src/cell/CCell.hpp

$(TMP_DIR)/CCommand.o: src/command/CCommand.cpp src/command/CCommand.hpp src/CSpreadSheet.hpp src/cell/CCellName.hpp
$(TMP_DIR)/CClearCommand.o: src/command/CClearCommand.cpp src/command/CClearCommand.hpp src/command/CCommand.hpp
$(TMP_DIR)/CExportCommand.o: src/command/CExportCommand.cpp src/command/CExportCommand.hpp src/command/CCommand.hpp
$(TMP_DIR)/CGetCommand.o: src/command/CGetCommand.cpp src/command/CGetCommand.hpp src/command/CCommand.hpp
$(TMP_DIR)/CLoadCommand.o: src/command/CLoadCommand.cpp src/command/CLoadCommand.hpp src/command/CCommand.hpp
$(TMP_DIR)/CPrintCommand.o: src/command/CPrintCommand.cpp src/command/CPrintCommand.hpp src/command/CCommand.hpp
$(TMP_DIR)/CSaveCommand.o: src/command/CSaveCommand.cpp src/command/CSaveCommand.hpp src/command/CCommand.hpp
$(TMP_DIR)/CSetCommand.o: src/command/CSetCommand.cpp src/command/CSetCommand.hpp src/command/CCommand.hpp src/cell/CExpressionCell.hpp src/cell/CStringCell.hpp