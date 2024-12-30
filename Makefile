
clean:
	-rm -rf generated/
	-rm -rf test_run_dir/
	-rm *.anno.json

wave:
	gtkwave ./test_run_dir/SystolicArray_should_perform_square_MM_operation/SystolicArray.vcd