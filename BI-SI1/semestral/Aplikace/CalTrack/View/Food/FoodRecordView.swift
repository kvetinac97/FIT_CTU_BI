//
//  FoodRecordView.swift
//  CalTrack
//
//  Created by Roman Isaev on 21/04/2021.
//

import SwiftUI

struct FoodRecordView: View {
    var userMealFood: UserMealFood
    @Binding var currentMealFood: UserMealFood
    @Binding var isPresented: Bool

    var body: some View {
        HStack {
            Button(
                action: {
                    currentMealFood = userMealFood
                    print("\(currentMealFood.meal.name), \(currentMealFood.date), \(currentMealFood.food.name)")
                    isPresented.toggle()
                }
            ) {
                ZStack {
                    Circle()
                        .foregroundColor(.white)
                        .frame(width: 20, height: 20 )
                    Image(systemName: "info.circle")
                        .resizable()
                        .frame(width: 20, height: 20)
                        .foregroundColor(Color.blue)
                }
            }
            Text(userMealFood.food.name)
                .font(.system(size: 18))
                .foregroundColor(.white)
            Spacer()
            Text("\(userMealFood.amount) x \(userMealFood.food.unit)")
                .font(.system(size: 18))
                .foregroundColor(.white)
        }
        .padding(.horizontal)
    }
}
