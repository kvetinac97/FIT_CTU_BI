
################################################################
# This is a generated script based on design: design_1
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
# source design_1_script.tcl

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
set design_name design_1

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
  set O_1 [ create_bd_port -dir O O_1 ]
  set a [ create_bd_port -dir I -type data a ]
  set an_0 [ create_bd_port -dir O an_0 ]
  set an_1 [ create_bd_port -dir O an_1 ]
  set an_2 [ create_bd_port -dir O an_2 ]
  set b [ create_bd_port -dir I -type data b ]
  set c [ create_bd_port -dir I -type data c ]
  set c_f [ create_bd_port -dir O -type data c_f ]
  set d [ create_bd_port -dir I -type data d ]
  set f_a [ create_bd_port -dir O -type data f_a ]
  set f_b [ create_bd_port -dir O -type data f_b ]
  set f_c [ create_bd_port -dir O -type data f_c ]
  set f_d [ create_bd_port -dir O -type data f_d ]
  set f_e [ create_bd_port -dir O -type data f_e ]
  set f_g [ create_bd_port -dir O -type data f_g ]

  # Create instance: an_3, and set properties
  set an_3 [ create_bd_cell -type ip -vlnv FIT:user:vcc:1.0 an_3 ]

  # Create instance: and0, and set properties
  set and0 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and0 ]
  set_property -dict [ list \
   CONFIG.I1_inverted {true} \
 ] $and0

  # Create instance: and1, and set properties
  set and1 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and1 ]
  set_property -dict [ list \
   CONFIG.I0_Inverted {true} \
   CONFIG.I2_inverted {true} \
   CONFIG.Inputs {3} \
 ] $and1

  # Create instance: and2, and set properties
  set and2 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and2 ]
  set_property -dict [ list \
   CONFIG.I1_inverted {true} \
   CONFIG.Inputs {3} \
 ] $and2

  # Create instance: and3, and set properties
  set and3 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and3 ]

  # Create instance: and4, and set properties
  set and4 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and4 ]
  set_property -dict [ list \
   CONFIG.I1_inverted {true} \
   CONFIG.Inputs {3} \
 ] $and4

  # Create instance: and5, and set properties
  set and5 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and5 ]
  set_property -dict [ list \
   CONFIG.Inputs {3} \
 ] $and5

  # Create instance: and6, and set properties
  set and6 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and6 ]
  set_property -dict [ list \
   CONFIG.I0_Inverted {true} \
   CONFIG.Inputs {3} \
 ] $and6

  # Create instance: and7, and set properties
  set and7 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and7 ]
  set_property -dict [ list \
   CONFIG.I0_Inverted {true} \
   CONFIG.I2_inverted {true} \
   CONFIG.I3_inverted {true} \
   CONFIG.Inputs {4} \
 ] $and7

  # Create instance: and8, and set properties
  set and8 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and8 ]
  set_property -dict [ list \
   CONFIG.Inputs {3} \
 ] $and8

  # Create instance: and9, and set properties
  set and9 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and9 ]
  set_property -dict [ list \
   CONFIG.I0_Inverted {true} \
   CONFIG.Inputs {3} \
 ] $and9

  # Create instance: and10, and set properties
  set and10 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and10 ]
  set_property -dict [ list \
   CONFIG.I1_inverted {true} \
   CONFIG.I2_inverted {true} \
   CONFIG.Inputs {3} \
 ] $and10

  # Create instance: and11, and set properties
  set and11 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and11 ]
  set_property -dict [ list \
   CONFIG.I0_Inverted {true} \
   CONFIG.I2_inverted {true} \
   CONFIG.Inputs {3} \
 ] $and11

  # Create instance: and12, and set properties
  set and12 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and12 ]
  set_property -dict [ list \
   CONFIG.Inputs {3} \
 ] $and12

  # Create instance: and13, and set properties
  set and13 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and13 ]
  set_property -dict [ list \
   CONFIG.I0_Inverted {true} \
   CONFIG.I2_inverted {true} \
   CONFIG.Inputs {4} \
 ] $and13

  # Create instance: and14, and set properties
  set and14 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and14 ]
  set_property -dict [ list \
   CONFIG.I1_inverted {true} \
 ] $and14

  # Create instance: and15, and set properties
  set and15 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and15 ]
  set_property -dict [ list \
   CONFIG.I1_inverted {true} \
   CONFIG.I2_inverted {true} \
   CONFIG.Inputs {3} \
 ] $and15

  # Create instance: and16, and set properties
  set and16 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and16 ]
  set_property -dict [ list \
   CONFIG.I0_Inverted {true} \
   CONFIG.I2_inverted {true} \
   CONFIG.Inputs {3} \
 ] $and16

  # Create instance: and17, and set properties
  set and17 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and17 ]
  set_property -dict [ list \
   CONFIG.I0_Inverted {true} \
   CONFIG.I1_inverted {true} \
   CONFIG.I2_inverted {true} \
   CONFIG.Inputs {3} \
 ] $and17

  # Create instance: and18, and set properties
  set and18 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and18 ]
  set_property -dict [ list \
   CONFIG.I0_Inverted {true} \
   CONFIG.I1_inverted {true} \
   CONFIG.Inputs {4} \
 ] $and18

  # Create instance: and19, and set properties
  set and19 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and19 ]
  set_property -dict [ list \
   CONFIG.I3_inverted {true} \
   CONFIG.Inputs {4} \
 ] $and19

  # Create instance: and20, and set properties
  set and20 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and20 ]
  set_property -dict [ list \
   CONFIG.I1_inverted {true} \
   CONFIG.I2_inverted {true} \
   CONFIG.Inputs {3} \
 ] $and20

  # Create instance: and21, and set properties
  set and21 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and21 ]
  set_property -dict [ list \
   CONFIG.I1_inverted {true} \
   CONFIG.I2_inverted {true} \
   CONFIG.Inputs {3} \
 ] $and21

  # Create instance: and22, and set properties
  set and22 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and22 ]
  set_property -dict [ list \
   CONFIG.I2_inverted {true} \
   CONFIG.Inputs {3} \
 ] $and22

  # Create instance: and23, and set properties
  set and23 [ create_bd_cell -type ip -vlnv FIT:user:and:1.0 and23 ]
  set_property -dict [ list \
   CONFIG.I1_inverted {true} \
   CONFIG.Inputs {4} \
 ] $and23

  # Create instance: gnd_0, and set properties
  set gnd_0 [ create_bd_cell -type ip -vlnv FIT:user:gnd:1.0 gnd_0 ]

  # Create instance: or0, and set properties
  set or0 [ create_bd_cell -type ip -vlnv FIT:user:or:1.0 or0 ]
  set_property -dict [ list \
   CONFIG.Inputs {3} \
 ] $or0

  # Create instance: or1, and set properties
  set or1 [ create_bd_cell -type ip -vlnv FIT:user:or:1.0 or1 ]
  set_property -dict [ list \
   CONFIG.Inputs {3} \
 ] $or1

  # Create instance: or2, and set properties
  set or2 [ create_bd_cell -type ip -vlnv FIT:user:or:1.0 or2 ]
  set_property -dict [ list \
   CONFIG.Inputs {3} \
 ] $or2

  # Create instance: or3, and set properties
  set or3 [ create_bd_cell -type ip -vlnv FIT:user:or:1.0 or3 ]
  set_property -dict [ list \
   CONFIG.Inputs {3} \
 ] $or3

  # Create instance: or4, and set properties
  set or4 [ create_bd_cell -type ip -vlnv FIT:user:or:1.0 or4 ]
  set_property -dict [ list \
   CONFIG.Inputs {3} \
 ] $or4

  # Create instance: or5, and set properties
  set or5 [ create_bd_cell -type ip -vlnv FIT:user:or:1.0 or5 ]
  set_property -dict [ list \
   CONFIG.Inputs {4} \
 ] $or5

  # Create instance: vcc_0, and set properties
  set vcc_0 [ create_bd_cell -type ip -vlnv FIT:user:vcc:1.0 vcc_0 ]

  # Create instance: vcc_1, and set properties
  set vcc_1 [ create_bd_cell -type ip -vlnv FIT:user:vcc:1.0 vcc_1 ]

  # Create instance: xor0, and set properties
  set xor0 [ create_bd_cell -type ip -vlnv FIT:user:xor:1.0 xor0 ]
  set_property -dict [ list \
   CONFIG.Inputs {3} \
 ] $xor0

  # Create instance: xor1, and set properties
  set xor1 [ create_bd_cell -type ip -vlnv FIT:user:xor:1.0 xor1 ]

  # Create instance: xor2, and set properties
  set xor2 [ create_bd_cell -type ip -vlnv FIT:user:xor:1.0 xor2 ]

  # Create port connections
  connect_bd_net -net I0_0_1 [get_bd_ports a] [get_bd_pins and0/I0] [get_bd_pins and10/I0] [get_bd_pins and12/I0] [get_bd_pins and13/I0] [get_bd_pins and14/I0] [get_bd_pins and15/I0] [get_bd_pins and18/I0] [get_bd_pins and19/I0] [get_bd_pins and2/I0] [get_bd_pins and21/I0] [get_bd_pins and22/I0] [get_bd_pins and23/I0] [get_bd_pins and4/I0] [get_bd_pins and5/I0] [get_bd_pins and6/I0] [get_bd_pins and7/I0] [get_bd_pins and9/I0]
  connect_bd_net -net I1_0_1 [get_bd_ports b] [get_bd_pins and0/I1] [get_bd_pins and1/I0] [get_bd_pins and10/I1] [get_bd_pins and11/I0] [get_bd_pins and12/I1] [get_bd_pins and13/I1] [get_bd_pins and15/I1] [get_bd_pins and16/I0] [get_bd_pins and17/I0] [get_bd_pins and18/I1] [get_bd_pins and19/I1] [get_bd_pins and20/I0] [get_bd_pins and22/I1] [get_bd_pins and23/I1] [get_bd_pins and4/I1] [get_bd_pins and5/I1] [get_bd_pins and6/I1] [get_bd_pins and7/I1] [get_bd_pins and8/I0]
  connect_bd_net -net I1_0_2 [get_bd_ports c] [get_bd_pins and1/I1] [get_bd_pins and11/I1] [get_bd_pins and12/I2] [get_bd_pins and13/I2] [get_bd_pins and15/I2] [get_bd_pins and16/I1] [get_bd_pins and17/I1] [get_bd_pins and18/I2] [get_bd_pins and19/I2] [get_bd_pins and2/I1] [get_bd_pins and20/I1] [get_bd_pins and21/I1] [get_bd_pins and23/I2] [get_bd_pins and3/I0] [get_bd_pins and4/I2] [get_bd_pins and6/I2] [get_bd_pins and7/I2] [get_bd_pins and8/I1] [get_bd_pins and9/I1]
  connect_bd_net -net I2_0_1 [get_bd_ports d] [get_bd_pins and1/I2] [get_bd_pins and10/I2] [get_bd_pins and11/I2] [get_bd_pins and13/I3] [get_bd_pins and14/I1] [get_bd_pins and16/I2] [get_bd_pins and17/I2] [get_bd_pins and18/I3] [get_bd_pins and19/I3] [get_bd_pins and2/I2] [get_bd_pins and20/I2] [get_bd_pins and21/I2] [get_bd_pins and22/I2] [get_bd_pins and23/I3] [get_bd_pins and3/I1] [get_bd_pins and5/I2] [get_bd_pins and7/I3] [get_bd_pins and8/I2] [get_bd_pins and9/I2]
  connect_bd_net -net and0_O [get_bd_pins and0/O] [get_bd_pins xor0/I0]
  connect_bd_net -net and10_O [get_bd_pins and10/O] [get_bd_pins xor2/I0]
  connect_bd_net -net and11_O [get_bd_pins and11/O] [get_bd_pins xor2/I1]
  connect_bd_net -net and12_O [get_bd_pins and12/O] [get_bd_pins or2/I1]
  connect_bd_net -net and13_O [get_bd_pins and13/O] [get_bd_pins or2/I2]
  connect_bd_net -net and14_O [get_bd_pins and14/O] [get_bd_pins or3/I0]
  connect_bd_net -net and15_O [get_bd_pins and15/O] [get_bd_pins or3/I1]
  connect_bd_net -net and16_O [get_bd_pins and16/O] [get_bd_pins or3/I2]
  connect_bd_net -net and17_O [get_bd_pins and17/O] [get_bd_pins or4/I0]
  connect_bd_net -net and18_O [get_bd_pins and18/O] [get_bd_pins or4/I1]
  connect_bd_net -net and19_O [get_bd_pins and19/O] [get_bd_pins or4/I2]
  connect_bd_net -net and1_O [get_bd_pins and1/O] [get_bd_pins xor0/I1]
  connect_bd_net -net and20_O [get_bd_pins and20/O] [get_bd_pins or5/I0]
  connect_bd_net -net and21_O [get_bd_pins and21/O] [get_bd_pins or5/I1]
  connect_bd_net -net and22_O [get_bd_pins and22/O] [get_bd_pins or5/I2]
  connect_bd_net -net and23_O [get_bd_pins and23/O] [get_bd_pins or5/I3]
  connect_bd_net -net and2_O [get_bd_pins and2/O] [get_bd_pins xor0/I2]
  connect_bd_net -net and3_O [get_bd_pins and3/O] [get_bd_pins xor1/I0]
  connect_bd_net -net and4_O [get_bd_pins and4/O] [get_bd_pins xor1/I1]
  connect_bd_net -net and5_O [get_bd_pins and5/O] [get_bd_pins or0/I1]
  connect_bd_net -net and6_O [get_bd_pins and6/O] [get_bd_pins or0/I2]
  connect_bd_net -net and7_O [get_bd_pins and7/O] [get_bd_pins or1/I0]
  connect_bd_net -net and8_O [get_bd_pins and8/O] [get_bd_pins or1/I1]
  connect_bd_net -net and9_O [get_bd_pins and9/O] [get_bd_pins or1/I2]
  connect_bd_net -net gnd_0_O [get_bd_ports an_0] [get_bd_pins gnd_0/O]
  connect_bd_net -net or0_O [get_bd_ports f_b] [get_bd_pins or0/O]
  connect_bd_net -net or1_O [get_bd_ports f_c] [get_bd_pins or1/O]
  connect_bd_net -net or2_O [get_bd_ports f_d] [get_bd_pins or2/O]
  connect_bd_net -net or3_O [get_bd_ports f_e] [get_bd_pins or3/O]
  connect_bd_net -net or4_O [get_bd_ports f_g] [get_bd_pins or4/O]
  connect_bd_net -net or5_O [get_bd_ports c_f] [get_bd_pins or5/O]
  connect_bd_net -net vcc_0_O [get_bd_ports an_1] [get_bd_pins vcc_0/O]
  connect_bd_net -net vcc_1_O [get_bd_ports an_2] [get_bd_pins vcc_1/O]
  connect_bd_net -net vcc_2_O [get_bd_ports O_1] [get_bd_pins an_3/O]
  connect_bd_net -net xor0_O [get_bd_ports f_a] [get_bd_pins xor0/O]
  connect_bd_net -net xor1_O [get_bd_pins or0/I0] [get_bd_pins xor1/O]
  connect_bd_net -net xor2_O [get_bd_pins or2/I0] [get_bd_pins xor2/O]

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


