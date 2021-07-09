
################################################################
# This is a generated script based on design: adder
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
# source adder_script.tcl


# The design that will be created by this Tcl script contains the following 
# module references:
# next_state_logic, output_logic

# Please add the sources of those modules before sourcing this Tcl script.

# If there is no project opened, this script will create a
# project, but make sure you do not have an existing project
# <./myproj/project_1.xpr> in the current working folder.

set list_projs [get_projects -quiet]
if { $list_projs eq "" } {
   create_project project_1 myproj -part xc7a12ticsg325-1L
}


# CHANGE DESIGN NAME HERE
variable design_name
set design_name adder

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
  set DIR [ create_bd_port -dir I DIR ]
  set INC [ create_bd_port -dir I INC ]
  set clk [ create_bd_port -dir I clk ]
  set reset [ create_bd_port -dir I reset ]
  set y0 [ create_bd_port -dir O y0 ]
  set y1 [ create_bd_port -dir O y1 ]
  set y2 [ create_bd_port -dir O y2 ]

  # Create instance: dff_0, and set properties
  set dff_0 [ create_bd_cell -type ip -vlnv FIT:user:dff:1.0 dff_0 ]

  # Create instance: dff_1, and set properties
  set dff_1 [ create_bd_cell -type ip -vlnv FIT:user:dff:1.0 dff_1 ]

  # Create instance: dff_2, and set properties
  set dff_2 [ create_bd_cell -type ip -vlnv FIT:user:dff:1.0 dff_2 ]

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
  
  # Create instance: output_logic_0, and set properties
  set block_name output_logic
  set block_cell_name output_logic_0
  if { [catch {set output_logic_0 [create_bd_cell -type module -reference $block_name $block_cell_name] } errmsg] } {
     catch {common::send_msg_id "BD_TCL-105" "ERROR" "Unable to add referenced block <$block_name>. Please add the files for ${block_name}'s definition into the project."}
     return 1
   } elseif { $output_logic_0 eq "" } {
     catch {common::send_msg_id "BD_TCL-106" "ERROR" "Unable to referenced block <$block_name>. Please add the files for ${block_name}'s definition into the project."}
     return 1
   }
  
  # Create port connections
  connect_bd_net -net DIR_0_1 [get_bd_ports DIR] [get_bd_pins next_state_logic_0/DIR] [get_bd_pins output_logic_0/DIR]
  connect_bd_net -net INC_0_1 [get_bd_ports INC] [get_bd_pins dff_0/ce] [get_bd_pins dff_1/ce] [get_bd_pins dff_2/ce] [get_bd_pins next_state_logic_0/INC] [get_bd_pins output_logic_0/INC]
  connect_bd_net -net clk_0_1 [get_bd_ports clk] [get_bd_pins dff_0/clk] [get_bd_pins dff_1/clk] [get_bd_pins dff_2/clk]
  connect_bd_net -net dff_0_q [get_bd_pins dff_0/q] [get_bd_pins next_state_logic_0/q0] [get_bd_pins output_logic_0/q0]
  connect_bd_net -net dff_1_q [get_bd_pins dff_1/q] [get_bd_pins next_state_logic_0/q1] [get_bd_pins output_logic_0/q1]
  connect_bd_net -net dff_2_q [get_bd_pins dff_2/q] [get_bd_pins next_state_logic_0/q2] [get_bd_pins output_logic_0/q2]
  connect_bd_net -net next_state_logic_0_d0 [get_bd_pins dff_0/d] [get_bd_pins next_state_logic_0/d0]
  connect_bd_net -net next_state_logic_0_d1 [get_bd_pins dff_1/d] [get_bd_pins next_state_logic_0/d1]
  connect_bd_net -net next_state_logic_0_d2 [get_bd_pins dff_2/d] [get_bd_pins next_state_logic_0/d2]
  connect_bd_net -net output_logic_0_y0 [get_bd_ports y0] [get_bd_pins output_logic_0/y0]
  connect_bd_net -net output_logic_0_y1 [get_bd_ports y1] [get_bd_pins output_logic_0/y1]
  connect_bd_net -net output_logic_0_y2 [get_bd_ports y2] [get_bd_pins output_logic_0/y2]
  connect_bd_net -net reset_0_1 [get_bd_ports reset] [get_bd_pins dff_0/reset] [get_bd_pins dff_1/reset] [get_bd_pins dff_2/reset]

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


