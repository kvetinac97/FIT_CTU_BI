//
//  FoodOrderItemView.swift
//  RestSys
//
//  Created by Thats Me on 21.01.2021.
//

import SwiftUI

struct FoodOrderItemView: View {
    
    let foodOrder: FoodOrder
    let decrementAction: (() -> ())?
    
    var body: some View {
        HStack {
            Text(foodOrder.food.name)
            Spacer()
            HStack {
                if foodOrder.count > 0 {
                    Text(String(foodOrder.count) + "x")
                }
                
                Text(String(foodOrder.food.price) + ",-")
                
                if decrementAction != nil && foodOrder.count > 0 {
                    Button (action: decrementAction!) {
                        Image(systemName: "minus.circle")
                            .foregroundColor(.blue)
                    }
                    .buttonStyle(BorderlessButtonStyle())
                }
            }
        }
        .listRowBackground(decrementAction != nil && foodOrder.count > 0 ? Color.green.opacity(0.2) : nil)
    }
    
}

struct FoodOrderItemView_Previews: PreviewProvider {
    static var previews: some View {
        FoodOrderItemView(foodOrder: FoodOrder.preview, decrementAction: nil)
    }
}
