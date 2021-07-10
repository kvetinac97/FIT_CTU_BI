//
//  TableDetailView.swift
//  RestSys
//
//  Created by Thats Me on 20.01.2021.
//

import SwiftUI

struct TableDetailView: View {
    
    @Environment(\.presentationMode) var presentationMode
    @StateObject var viewModel = TableDetailViewModel()
    
    @State var isShowing = false
    @State var pdfDialog = false
    
    let table: Table
    
    var body: some View {
        CRUDView<FoodOrder, ZStack>(viewModel: viewModel, reload: reload) {
            ZStack {
                if viewModel.firstLoaded && !viewModel.entities.isEmpty {
                    List {
                        ForEach(viewModel.entities) { foodOrder in
                            FoodOrderItemView(foodOrder: foodOrder, decrementAction: nil)
                                .padding([.top, .bottom], 4)
                        }
                        
                        HStack {
                            Text("Total:")
                                .font(.headline)
                            Spacer()
                            Text(String(viewModel.entities.map({$0.count * $0.food.price}).reduce(0, +)) + ",-")
                        }
                    }
                    
                    TableDetailViewBottom(pdfDialog: $pdfDialog, trashOrder: trashOrder, submitOrder: submitOrder)
                }
            }
        }
        .navigationTitle(Text("Table #" + String(table.id)))
        .navigationBarItems(
            trailing: NavigationLink(destination: TableEditView(table: table)) { Image(systemName: "pencil") }
        )
        .sheet(isPresented: $pdfDialog) {
            PDFViewController(viewModel: viewModel)
        }
    }
}

extension TableDetailView {
    func reload () {
        viewModel.loadFood(table: table)
    }
    
    func dismiss () {
        presentationMode.wrappedValue.dismiss()
    }
    
    func trashOrder () {
        if let orderId = viewModel.entities.first?.order {
            viewModel.trashOrder(orderId: orderId, table: table, dismiss: dismiss)
        }
    }
    
    func submitOrder () {
        if let orderId = viewModel.entities.first?.order {
            viewModel.submitOrder(orderId: orderId, table: table, dismiss: dismiss)
        }
    }
}

struct TableDetailView_Previews: PreviewProvider {
    static var previews: some View {
        TableDetailView(table: Table.preview)
    }
}
