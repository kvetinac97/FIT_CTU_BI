//
//  TableCreateView.swift
//  RestSys
//
//  Created by Thats Me on 20.01.2021.
//

import SwiftUI

struct TableCreateView: View {
    
    @Environment(\.presentationMode) var presentationMode
    @StateObject var viewModel = TableCreateViewModel()
        
    var body: some View {
        NavigationView {
            ZStack {
                VStack {
                    Picker("Table type", selection: $viewModel.type) {
                        Text("Bar").tag("BAR")
                        Text("Inside").tag("INSIDE")
                        Text("Outside").tag("OUTSIDE")
                    }
                    Spacer()
                }
            }
            .navigationTitle(Text("Create"))
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarItems(
                trailing: Button(action: viewModel.createTable) {
                    Image(systemName: "plus")
                }
            )
            .modifier(CreateViewModifier(viewModel: viewModel, successAction: dismiss))
        }
    }
}

extension TableCreateView {
    func dismiss () {
        presentationMode.wrappedValue.dismiss()
    }
}

struct TableCreateView_Previews: PreviewProvider {
    static var previews: some View {
        TableCreateView()
    }
}
