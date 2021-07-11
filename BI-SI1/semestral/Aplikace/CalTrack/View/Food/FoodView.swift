//
//  FoodView.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 21.04.2021.
//

import SwiftUI

struct FoodView: View {
    var food: Food
    var foodCalories: Int
    
    var body: some View {
        HStack {
            Text(food.name)
                .font(.system(size: 18))
                .foregroundColor(.white)
            Spacer()
            Text("\(foodCalories) kcal / 100g")
                .font(.system(size: 18))
                .foregroundColor(.white)
        }
    }
}
