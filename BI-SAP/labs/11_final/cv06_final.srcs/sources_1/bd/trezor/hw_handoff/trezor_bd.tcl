
################################################################
# This is a generated script based on design: trezor
#
# Though there are limitations about the generated script,
# the main purpose of this utility is to make learning
# IP Integrator Tcl commands easier.
################################################################

namespace eval _tcl {
proc get_script_folder {} {
   set script_path [file normalize [info script]]
   set script_folder [file dirname $script_path]
   return $script_folder
}
}
variable script_folder
set script_folder [_tcl::get_script_folder]

################################################################
# Check if script is running in correct Vivado version.
################################################################
set scripts_vivado_version 2018.2
set current_vivado_version [version -short]

if { [string first $scripts_vivado_version $current_vivado_version] == -1 } {
   puts ""
   catch {common::send_msg_id "BD_TCL-109" "ERROR" "This script was generated using Vivado <$scripts_vivado_version> and is being run in <$current_vivado_version> of Vivado. Please run the script in Vivado <$scripts_vivado_version> then open the design in Vivado <$current_vivado_version>. Upgrade the design by running \"Tools => Report => Report IP Status...\", then run write_bd_tcl to create an updated script."}

   return 1
}

################################################################
# START
################################################################

# To test this script, run the following commands from Vivado Tcl console:
# source trezor_script.tcl


# The design that will be created by this Tcl script contains the following 
# module references:
# next_state_logic, trezorKoder

# Please add the sources of those modules before sourcing this Tcl script.

# If there is no project opened, this script will create a
# project, but make sure you do not have an existing project
# <./myproj/project_1.xpr> in the current working folder.

set list_projs [get_projects -quiet]
if { $list_projs eq "" } {
   create_project project_1 myproj -part xc7a200tfbg676-2
   set_property BOARD_PART xilinx.com:ac701:part0:1.4 [current_project]
}


# CHANGE DESIGN NAME HERE
variable design_name
set design_name trezor

# If you do not already have an existing IP Integrator design open,
# you can create a design using the following command:
#    create_bd_design $design_name

# Creating design if needed
set errMsg ""
set nRet 0

set cur_design [current_bd_design -quiet]
set list_cells [get_bd_cells -quiet]

if { ${design_name} eq "" } {
   # USE CASES:
   #    1) Design_name not set

   set errMsg "Please set the variable <design_name> to a non-empty value."
   set nRet 1

} elseif { ${cur_design} ne "" && ${list_cells} eq "" } {
   # USE CASES:
   #    2): Current design opened AND is empty AND names same.
   #    3): Current design opened AND is empty AND names diff; design_name NOT in project.
   #    4): Current design opened AND is empty AND names diff; design_name exists in project.

   if { $cur_design ne $design_name } {
      common::send_msg_id "BD_TCL-001" "INFO" "Changing value of <design_name> from <$design_name> to <$cur_design> since current design is empty."
      set design_name [get_property NAME $cur_design]
   }
   common::send_msg_id "BD_TCL-002" "INFO" "Constructing design in IPI design <$cur_design>..."

} elseif { ${cur_design} ne "" && $list_cells ne "" && $cur_design eq $design_name } {
   # USE CASES:
   #    5) Current design opened AND has components AND same names.

   set errMsg "Design <$design_name> already exists in your project, please set the variable <design_name> to another value."
   set nRet 1
} elseif { [get_files -quiet ${design_name}.bd] ne "" } {
   # USE CASES: 
   #    6) Current opened design, has components, but diff names, design_name exists in project.
   #    7) No opened design, design_name exists in project.

   set errMsg "Design <$design_name> already exists in your project, please set the variable <design_name> to another value."
   set nRet 2

} else {
   # USE CASES:
   #    8) No opened design, design_name not in project.
   #    9) Current opened design, has components, but diff names, design_name not in project.

   common::send_msg_id "BD_TCL-003" "INFO" "Currently there is no design <$design_name> in project, so creating one..."

   create_bd_design $design_name

   common::send_msg_id "BD_TCL-004" "INFO" "Making design <$design_name> as current_bd_design."
   current_bd_design $design_name

}

common::send_msg_id "BD_TCL-005" "INFO" "Currently the variable <design_name> is equal to \"$design_name\"."

if { $nRet != 0 } {
   catch {common::send_msg_id "BD_TCL-114" "ERROR" $errMsg}
   return $nRet
}

##################################################################
# DESIGN PROCs
##################################################################


# Hierarchical cell: automat_0
proc create_hier_cell_automat_0 { parentCell nameHier } {

  variable script_folder

  if { $parentCell eq "" || $nameHier eq "" } {
     catch {common::send_msg_id "BD_TCL-102" "ERROR" "create_hier_cell_automat_0() - Empty argument(s)!"}
     return
  }

  # Get object for parentCell
  set parentObj [get_bd_cells $parentCell]
  if { $parentObj == "" } {
     catch {common::send_msg_id "BD_TCL-100" "ERROR" "Unable to find parent cell <$parentCell>!"}
     return
  }

  # Make sure parentObj is hier blk
  set parentType [get_property TYPE $parentObj]
  if { $parentType ne "hier" } {
     catch {common::send_msg_id "BD_TCL-101" "ERROR" "Parent <$parentObj> has TYPE = <$parentType>. Expected to be <hier>."}
     return
  }

  # Save current instance; Restore later
  set oldCurInst [current_bd_instance .]

  # Set parent object as current
  current_bd_instance $parentObj

  # Create cell and set as current instance
  set hier_obj [create_bd_cell -type hier $nameHier]
  current_bd_instance $hier_obj

  # Create interface pins

  # Create pins
  create_bd_pin -dir I -type data I0_0
  create_bd_pin -dir I clk_0
  create_bd_pin -dir I down_0
  create_bd_pin -dir I left_0
  create_bd_pin -dir I reset_0
  create_bd_pin -dir I right_0
  create_bd_pin -dir O tb0
  create_bd_pin -dir O tb1
  create_bd_pin -dir O -type data ts1
  create_bd_pin -dir I up_0
  create_bd_pin -dir O -type data y

  # Create instance: dff_0, and set properties
  set dff_0 [ create_bd_cell -type ip -vlnv FIT:user:dff:1.0 dff_0 ]

  # Create instance: dff_1, and set properties
  set dff_1 [ create_bd_cell -type ip -vlnv FIT:user:dff:1.0 dff_1 ]

  # Create instance: dff_2, and set properties
  set dff_2 [ create_bd_cell -type ip -vlnv FIT:user:dff:1.0 dff_2 ]
  set_property -dict [ list \
   CONFIG.CE {false} \
   CONFIG.Reset {false} \
 ] $dff_2

  # Create instance: edge_detector, and set properties
  set edge_detector [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 edge_detector ]
  set_property -dict [ list \
   CONFIG.I1_inverted {true} \
 ] $edge_detector

  # Create instance: next_state_logic_0, and set properties
  set block_name next_state_logic
  set block_cell_name next_state_logic_0
  if { [catch {set next_state_logic_0 [create_bd_cell -type module -reference $block_name $block_cell_name] } errmsg] } {
     catch {common::send_msg_id "BD_TCL-105" "ERROR" "Unable to add referenced block <$block_name>. Please add the files for ${block_name}'s definition into the project."}
     return 1
   } elseif { $next_state_logic_0 eq "" } {
     catch {common::send_msg_id "BD_TCL-106" "ERROR" "Unable to referenced block <$block_name>. Please add the files for ${block_name}'s definition into the project."}
     return 1
   }
  
  # Create instance: output_state_logic, and set properties
  set output_state_logic [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 output_state_logic ]

  # Create instance: trezorKoder_0, and set properties
  set block_name trezorKoder
  set block_cell_name trezorKoder_0
  if { [catch {set trezorKoder_0 [create_bd_cell -type module -reference $block_name $block_cell_name] } errmsg] } {
     catch {common::send_msg_id "BD_TCL-105" "ERROR" "Unable to add referenced block <$block_name>. Please add the files for ${block_name}'s definition into the project."}
     return 1
   } elseif { $trezorKoder_0 eq "" } {
     catch {common::send_msg_id "BD_TCL-106" "ERROR" "Unable to referenced block <$block_name>. Please add the files for ${block_name}'s definition into the project."}
     return 1
   }
  
  # Create port connections
  connect_bd_net -net I0_0_1 [get_bd_pins I0_0] [get_bd_pins dff_2/d] [get_bd_pins edge_detector/I0]
  connect_bd_net -net clk_0_1 [get_bd_pins clk_0] [get_bd_pins dff_0/clk] [get_bd_pins dff_1/clk] [get_bd_pins dff_2/clk]
  connect_bd_net -net dff_0_q [get_bd_pins ts1] [get_bd_pins dff_0/q] [get_bd_pins next_state_logic_0/q0] [get_bd_pins output_state_logic/I1]
  connect_bd_net -net dff_1_q [get_bd_pins dff_1/q] [get_bd_pins next_state_logic_0/q1] [get_bd_pins output_state_logic/I0]
  connect_bd_net -net dff_2_q [get_bd_pins dff_2/q] [get_bd_pins edge_detector/I1]
  connect_bd_net -net down_0_1 [get_bd_pins down_0] [get_bd_pins trezorKoder_0/down]
  connect_bd_net -net edge_detector_O [get_bd_pins dff_0/ce] [get_bd_pins dff_1/ce] [get_bd_pins edge_detector/O]
  connect_bd_net -net left_0_1 [get_bd_pins left_0] [get_bd_pins trezorKoder_0/left]
  connect_bd_net -net next_state_logic_0_d0 [get_bd_pins dff_0/d] [get_bd_pins next_state_logic_0/d0]
  connect_bd_net -net next_state_logic_0_d1 [get_bd_pins dff_1/d] [get_bd_pins next_state_logic_0/d1]
  connect_bd_net -net output_state_logic_O [get_bd_pins y] [get_bd_pins output_state_logic/O]
  connect_bd_net -net reset_0_1 [get_bd_pins reset_0] [get_bd_pins dff_0/reset] [get_bd_pins dff_1/reset]
  connect_bd_net -net right_0_1 [get_bd_pins right_0] [get_bd_pins trezorKoder_0/right]
  connect_bd_net -net trezorKoder_0_buttons0 [get_bd_pins tb0] [get_bd_pins next_state_logic_0/buttons0] [get_bd_pins trezorKoder_0/buttons0]
  connect_bd_net -net trezorKoder_0_buttons1 [get_bd_pins tb1] [get_bd_pins next_state_logic_0/buttons1] [get_bd_pins trezorKoder_0/buttons1]
  connect_bd_net -net up_0_1 [get_bd_pins up_0] [get_bd_pins trezorKoder_0/up]

  # Restore current instance
  current_bd_instance $oldCurInst
}


# Procedure to create entire design; Provide argument to make
# procedure reusable. If parentCell is "", will use root.
proc create_root_design { parentCell } {

  variable script_folder
  variable design_name

  if { $parentCell eq "" } {
     set parentCell [get_bd_cells /]
  }

  # Get object for parentCell
  set parentObj [get_bd_cells $parentCell]
  if { $parentObj == "" } {
     catch {common::send_msg_id "BD_TCL-100" "ERROR" "Unable to find parent cell <$parentCell>!"}
     return
  }

  # Make sure parentObj is hier blk
  set parentType [get_property TYPE $parentObj]
  if { $parentType ne "hier" } {
     catch {common::send_msg_id "BD_TCL-101" "ERROR" "Parent <$parentObj> has TYPE = <$parentType>. Expected to be <hier>."}
     return
  }

  # Save current instance; Restore later
  set oldCurInst [current_bd_instance .]

  # Set parent object as current
  current_bd_instance $parentObj


  # Create interface ports

  # Create ports
  set clk [ create_bd_port -dir I clk ]
  set down [ create_bd_port -dir I down ]
  set left [ create_bd_port -dir I left ]
  set reset [ create_bd_port -dir I reset ]
  set right [ create_bd_port -dir I right ]
  set tb0 [ create_bd_port -dir O tb0 ]
  set tb1 [ create_bd_port -dir O tb1 ]
  set ts0 [ create_bd_port -dir O ts0 ]
  set ts1 [ create_bd_port -dir O ts1 ]
  set up [ create_bd_port -dir I up ]
  set y [ create_bd_port -dir O -type data y ]

  # Create instance: automat_0
  create_hier_cell_automat_0 [current_bd_instance .] automat_0

  # Create instance: debounce_0, and set properties
  set debounce_0 [ create_bd_cell -type ip -vlnv FIT:user:debounce:1.0 debounce_0 ]

  # Create instance: debounce_1, and set properties
  set debounce_1 [ create_bd_cell -type ip -vlnv FIT:user:debounce:1.0 debounce_1 ]

  # Create instance: debounce_2, and set properties
  set debounce_2 [ create_bd_cell -type ip -vlnv FIT:user:debounce:1.0 debounce_2 ]

  # Create instance: debounce_3, and set properties
  set debounce_3 [ create_bd_cell -type ip -vlnv FIT:user:debounce:1.0 debounce_3 ]

  # Create instance: or_0, and set properties
  set or_0 [ create_bd_cell -type ip -vlnv FIT:user:or:1.0 or_0 ]
  set_property -dict [ list \
   CONFIG.Inputs {4} \
 ] $or_0

  # Create port connections
  connect_bd_net -net I0_0_1 [get_bd_pins automat_0/I0_0] [get_bd_pins or_0/O]
  connect_bd_net -net automat_0_tb0 [get_bd_ports tb0] [get_bd_pins automat_0/tb0]
  connect_bd_net -net automat_0_tb1 [get_bd_ports tb1] [get_bd_pins automat_0/tb1]
  connect_bd_net -net automat_0_ts1 [get_bd_ports ts0] [get_bd_ports ts1] [get_bd_pins automat_0/ts1]
  connect_bd_net -net clk_0_1 [get_bd_ports clk] [get_bd_pins automat_0/clk_0] [get_bd_pins debounce_0/clk] [get_bd_pins debounce_1/clk] [get_bd_pins debounce_2/clk] [get_bd_pins debounce_3/clk]
  connect_bd_net -net down_0_1 [get_bd_pins automat_0/down_0] [get_bd_pins debounce_2/tl_out]
  connect_bd_net -net left_0_1 [get_bd_pins automat_0/left_0] [get_bd_pins debounce_3/tl_out]
  connect_bd_net -net output_state_logic_O [get_bd_ports y] [get_bd_pins automat_0/y]
  connect_bd_net -net reset_0_1 [get_bd_ports reset] [get_bd_pins automat_0/reset_0]
  connect_bd_net -net right_0_1 [get_bd_pins automat_0/right_0] [get_bd_pins debounce_1/tl_out]
  connect_bd_net -net tl_in_0_1 [get_bd_ports up] [get_bd_pins debounce_0/tl_in] [get_bd_pins or_0/I0]
  connect_bd_net -net tl_in_0_2 [get_bd_ports right] [get_bd_pins debounce_1/tl_in] [get_bd_pins or_0/I1]
  connect_bd_net -net tl_in_0_3 [get_bd_ports down] [get_bd_pins debounce_2/tl_in] [get_bd_pins or_0/I2]
  connect_bd_net -net tl_in_0_4 [get_bd_ports left] [get_bd_pins debounce_3/tl_in] [get_bd_pins or_0/I3]
  connect_bd_net -net up_0_1 [get_bd_pins automat_0/up_0] [get_bd_pins debounce_0/tl_out]

  # Create address segments


  # Restore current instance
  current_bd_instance $oldCurInst

  save_bd_design
}
# End of create_root_design()


##################################################################
# MAIN FLOW
##################################################################

create_root_design ""


