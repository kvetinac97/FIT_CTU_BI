//
//  FoodCreateView.swift
//  RestSys
//
//  Created by Thats Me on 14.01.2021.
//

import SwiftUI

struct FoodCreateView: View {
    
    @Environment(\.presentationMode) var presentationMode
    @StateObject var viewModel: FoodCreateViewModel
    
    var body: some View {
        FoodCreateForm(viewModel: viewModel)
            .navigationBarBackButtonHidden(true)
            .navigationBarTitle(Text(viewModel.id == nil ? "Create" : "Update"))
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarItems(
                leading: Button(action: dismiss) {
                    Image(systemName: "xmark")
                },
                trailing: Group {
                    switch viewModel.state {
                        case .fresh, .done:
                            Button (action: createOrUpdate) {
                                Image(systemName: "checkmark")
                            }
                        case .busy:
                            ProgressView()
                                .progressViewStyle(CircularProgressViewStyle())
                        }
                }
            )
            .modifier(CreateViewModifier(viewModel: viewModel, successAction: dismiss))
    }
}

extension FoodCreateView {
    func createOrUpdate () {
        viewModel.createOrUpdateFood()
    }
    
    func dismiss () {
        presentationMode.wrappedValue.dismiss()
    }
}

struct FoodCreateView_Previews: PreviewProvider {
    static var previews: some View {
        FoodCreateView(viewModel: FoodCreateViewModel())
    }
}
