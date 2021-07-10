//
//  FoodItemView.swift
//  RestSys
//
//  Created by Thats Me on 26.12.2020.
//

import SwiftUI

struct FoodItemView: View {
    
    let foodItem: Food
    let reload: () -> ()
    
    @EnvironmentObject var user: AuthUser
    @State var editFoodPresented = false
    
    var body: some View {
        VStack(alignment: .trailing, spacing: 6) {
            HStack {
                Text(foodItem.name)
                    .font(.headline)
                Spacer()
                Text(String(foodItem.price) + ",-")
                if user.admin {
                    Button (action: editAction) {
                        Image(systemName: "pencil")
                            .foregroundColor(.blue)
                    }
                    .buttonStyle(BorderlessButtonStyle())
                }
            }
            
            if !foodItem.allergens.isEmpty {
                NavigationLink (
                    destination: AllergensView (allergenList: AllergenViewModel(food: foodItem.id))
                ) { Text("Allergens: " + (foodItem.allergens.prefix(5).map {String($0)}).joined(separator: ", ") + ( foodItem.allergens.count > 5 ? "..." : "" )) }
                .tag("Alergen" + String(foodItem.id))
            }
        }
        .padding([.leading, .trailing])
        .sheet(isPresented: $editFoodPresented, onDismiss: reload, content: {
            NavigationView {
                FoodCreateView(viewModel: FoodCreateViewModel(food: foodItem))
            }
        })
    }
}

extension FoodItemView {
    func editAction () {
        editFoodPresented = true
    }
}

struct FoodItemView_Previews: PreviewProvider {
    static var previews: some View {
        FoodItemView(foodItem: Food.preview) {}
    }
}
