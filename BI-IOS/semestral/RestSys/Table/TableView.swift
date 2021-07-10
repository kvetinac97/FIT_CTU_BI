//
//  TableView.swift
//  RestSys
//
//  Created by Thats Me on 20.01.2021.
//

import SwiftUI
import MultiSelectSegmentedControl

struct TableView: View {
    
    @StateObject var tableList = TableViewModel()
    @EnvironmentObject var user: AuthUser
    
    @State var createTablePresented = false
    @State var navigationLinkActive = false
    
    @State var editDeletePresented = false
    @State var selectedTable: Table? = nil
    
    var body: some View {
        CRUDView<Table, ScrollView>(viewModel: tableList, reload: tableList.loadTables) {
            ScrollView {
                LazyVGrid(columns: [GridItem(.adaptive(minimum: 80))]) {
                    ForEach(tableList.entities) { table in
                        TableItemView(table: table, reload: tableList.loadTables)
                            .onTapGesture {
                                navigationLinkActive = true
                                selectedTable = table
                            }
                            .onLongPressGesture {
                                if user.admin {
                                    editDeletePresented = true
                                    selectedTable = table
                                }
                            }
                    }
                    
                    NavigationLink(destination: selectedTable == nil ? AnyView(EmptyView()) : (
                        selectedTable!.empty ? AnyView(TableEditView(table: selectedTable!)) : AnyView(TableDetailView(table: selectedTable!))
                    ), isActive: $navigationLinkActive) { EmptyView() }
                }
                .padding([.top])
                .alert(isPresented: $editDeletePresented) {
                    Alert(title: Text("Table #" + String(selectedTable!.id)), primaryButton: Alert.Button.cancel(), secondaryButton: Alert.Button.destructive(Text("Delete"), action: deleteSelected))
                }
            }
        }
        .navigationTitle(Text("Tables"))
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarItems(
            leading: Button(action: tableList.loadTables) {
                Image(systemName: "arrow.clockwise")
            },
            trailing: user.admin ? AnyView(Button(action: showCreateTable) { Image(systemName: "plus") }) : AnyView(EmptyView())
        )
        .sheet(isPresented: $createTablePresented, onDismiss: tableList.loadTables) {
            TableCreateView()
        }
    }
}

extension TableView {
    func showCreateTable () {
        createTablePresented = true
    }
    
    func deleteSelected () {
        tableList.removeTable(table: selectedTable!)
    }
}

struct TableView_Previews: PreviewProvider {
    static var previews: some View {
        TableView()
    }
}
