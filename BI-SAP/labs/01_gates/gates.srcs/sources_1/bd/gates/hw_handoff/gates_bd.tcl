
################################################################
# This is a generated script based on design: gates
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
# source gates_script.tcl

# If there is no project opened, this script will create a
# project, but make sure you do not have an existing project
# <./myproj/project_1.xpr> in the current working folder.

set list_projs [get_projects -quiet]
if { $list_projs eq "" } {
   create_project project_1 myproj -part xc7a35tcpg236-1
   set_property BOARD_PART digilentinc.com:basys3:part0:1.1 [current_project]
}


# CHANGE DESIGN NAME HERE
variable design_name
set design_name gates

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
  set A [ create_bd_port -dir I -type data A ]
  set B [ create_bd_port -dir I -type data B ]
  set C [ create_bd_port -dir I -type data C ]
  set D [ create_bd_port -dir I -type data D ]
  set E [ create_bd_port -dir I -type data E ]
  set F [ create_bd_port -dir I -type data F ]
  set G [ create_bd_port -dir I -type data G ]
  set H [ create_bd_port -dir I -type data H ]
  set P [ create_bd_port -dir O -type data P ]
  set Q [ create_bd_port -dir O -type data Q ]
  set R [ create_bd_port -dir O -type data R ]
  set S [ create_bd_port -dir O -type data S ]

  # Create instance: and_0, and set properties
  set and_0 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and_0 ]

  # Create instance: nand_0, and set properties
  set nand_0 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 nand_0 ]
  set_property -dict [ list \
   CONFIG.O_inverted {true} \
 ] $nand_0

  # Create instance: or_0, and set properties
  set or_0 [ create_bd_cell -type ip -vlnv FIT:user:or:1.0 or_0 ]

  # Create instance: xor_0, and set properties
  set xor_0 [ create_bd_cell -type ip -vlnv FIT:user:xor:1.0 xor_0 ]

  # Create port connections
  connect_bd_net -net I0_0_1 [get_bd_ports A] [get_bd_pins and_0/I0]
  connect_bd_net -net I0_1_1 [get_bd_ports E] [get_bd_pins or_0/I0]
  connect_bd_net -net I0_2_1 [get_bd_ports C] [get_bd_pins nand_0/I0]
  connect_bd_net -net I0_3_1 [get_bd_ports G] [get_bd_pins xor_0/I0]
  connect_bd_net -net I1_0_1 [get_bd_ports B] [get_bd_pins and_0/I1]
  connect_bd_net -net I1_1_1 [get_bd_ports F] [get_bd_pins or_0/I1]
  connect_bd_net -net I1_2_1 [get_bd_ports D] [get_bd_pins nand_0/I1]
  connect_bd_net -net I1_3_1 [get_bd_ports H] [get_bd_pins xor_0/I1]
  connect_bd_net -net and_0_O [get_bd_ports P] [get_bd_pins and_0/O]
  connect_bd_net -net nand_0_O [get_bd_ports Q] [get_bd_pins nand_0/O]
  connect_bd_net -net or_0_O [get_bd_ports R] [get_bd_pins or_0/O]
  connect_bd_net -net xor_0_O [get_bd_ports S] [get_bd_pins xor_0/O]

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


