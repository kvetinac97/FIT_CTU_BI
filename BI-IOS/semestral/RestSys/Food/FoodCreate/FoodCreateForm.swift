//
//  FoodCreateForm.swift
//  RestSys
//
//  Created by Thats Me on 18.01.2021.
//

import SwiftUI
import MultiSelectSegmentedControl

struct FoodCreateForm: View {
    
    @StateObject var viewModel: FoodCreateViewModel
    
    var body: some View {
        Form {
            TextFieldView(title: "Name", content: $viewModel.name)
            
            TextFieldView(title: "Price", content: $viewModel.priceString)
                .keyboardType(.numberPad)
            
            Toggle(isOn: $viewModel.cooked) {
                Text("Cooked")
                    .font(.headline)
            }
            
            VStack (alignment: .leading) {
                Text("Allergens")
                    .font(.headline)
                
                MultiSegmentPicker(
                    selectedSegmentIndexes: $viewModel.allergens,
                    items: Allergen.list,
                    isVertical: true
                )
            }
            .padding([.bottom])
        }
    }
}

struct FoodCreateForm_Previews: PreviewProvider {
    static var previews: some View {
        FoodCreateForm(
            viewModel: FoodCreateViewModel()
        )
    }
}
