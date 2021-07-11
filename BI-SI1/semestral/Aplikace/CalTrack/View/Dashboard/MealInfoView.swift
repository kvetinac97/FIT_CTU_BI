//
//  MealInfoView.swift
//  CalTrack
//
//  Created by Roman Isaev on 02/05/2021.
//

import SwiftUI

struct FoodMealInfoRow: View {
    var name: String
    var description: String
    
    var body: some View {
        Divider()
        HStack {
            Text("\(name)")
                .bold()
            Spacer()
            Text("\(description)")
        }
        .padding(.horizontal)
    }
}

struct MealInfoView: View {
    @State var userMealFood: UserMealFood
    @ObservedObject var viewModel: DashboardViewModel

    var body: some View {
        ZStack {
            Color(UIColor.darkGray)
            RoundedRectangle(cornerRadius: 20)
                .stroke(
                    LinearGradient(
                        gradient: Gradient( colors: [.blue, .white, .red, .blue] ),
                        startPoint: .leading,
                        endPoint: .trailing),
                    lineWidth: 1.2)
                .frame(width: 330, height: 350)
            VStack {
                Text("\(userMealFood.food.name)")
                    .bold()
                    .font(.system(size: 20))
                    .padding()
                FoodMealInfoRow(name: "Chod:",
                                description: "\(userMealFood.meal.name)")
                FoodMealInfoRow(name: "Sacharidy:",
                                description: "\(userMealFood.food.carbohydrates) g")
                FoodMealInfoRow(name: "Bílkoviny:",
                                description: "\(userMealFood.food.protein) g")
                FoodMealInfoRow(name: "Tuky:",
                                description: "\(userMealFood.food.fat) g")
                FoodMealInfoRow(name: "Kcal:",
                                description: "\(viewModel.caloryService.caloriesInFoodMealList(foodMealList: [userMealFood]).calories)")
                FoodMealInfoRow(name: "Množství:",
                                description: "\(userMealFood.amount) x \(userMealFood.food.unit)")
            }
            .foregroundColor(.white)
            .padding()
        }
        .frame(width: 330, height: 350, alignment: .center)
        .cornerRadius(20)
    }
}
