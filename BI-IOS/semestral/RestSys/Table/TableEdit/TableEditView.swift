//
//  TableEditView.swift
//  RestSys
//
//  Created by Thats Me on 21.01.2021.
//

import SwiftUI

struct TableEditView: View {
    
    @Environment(\.presentationMode) var presentationMode
    @StateObject var viewModel = TableEditViewModel()
    @State var isShowing = false
    
    let table: Table
    
    var body: some View {
        ZStack {
            VStack {
                List {
                    ForEach(viewModel.food.keys.sorted { viewModel.food[$0]!.food.name < viewModel.food[$1]!.food.name }, id: \.self) { foodId in
                        FoodOrderItemView(foodOrder: viewModel.food[foodId]!, decrementAction: { viewModel.food[foodId]!.count -= 1 })
                            .contentShape(Rectangle())
                            .onTapGesture {
                                viewModel.food[foodId]!.count += 1
                            }
                            .padding([.top, .bottom], 4)
                    }
                }
                .pullToRefresh(isShowing: $isShowing) {
                    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                        reload()
                    }
                }
            }
            
            if !viewModel.firstLoaded {
                ProgressView()
            }
            
            if viewModel.firstLoaded && viewModel.food.isEmpty {
                Text("No food available")
                    .padding([.leading, .trailing])
            }
        }
        .navigationTitle(Text("Table #" + String(table.id)))
        .navigationBarItems(
            trailing: viewModel.food.filter({$0.1.count > 0}).isEmpty ? AnyView(EmptyView()) : AnyView(Button(action: saveChanges) { Image(systemName: "checkmark") })
        )
        .onAppear {
            if !viewModel.firstLoaded {
                reload()
            }
        }
        .alert(item: $viewModel.errorMessage) { _ in
            Alert(title: Text("Error"), message: Text(viewModel.errorMessage!.localizedDescription), primaryButton: Alert.Button.default(Text("Reload"), action: reload), secondaryButton: Alert.Button.cancel(Text("Cancel"), action: {viewModel.firstLoaded = true}))
        }
    }
    
}

extension TableEditView {
    func reload () {
        viewModel.loadFood(table: table)
    }
    
    func dismiss () {
        presentationMode.wrappedValue.dismiss()
    }
    
    func saveChanges () {
        viewModel.editFood(table: table, dismiss: dismiss)
    }
}

struct TableEditView_Previews: PreviewProvider {
    static var previews: some View {
        TableEditView(table: Table.preview)
    }
}
