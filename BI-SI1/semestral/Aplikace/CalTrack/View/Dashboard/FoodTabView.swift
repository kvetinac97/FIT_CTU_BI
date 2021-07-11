//
//  FoodTabView.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 03.05.2021.
//

import SwiftUI

struct FoodTabView: View {
    @ObservedObject var viewModel: DashboardViewModel
    @Binding var foodTabShown: Bool
    @State var isDeleteFoodPresented: Bool = false
    @Binding var isInfoPresented: Bool
    @Binding var currentMealFood: UserMealFood
    
    var body: some View {
        VStack {
            HStack {
                PlusButton(
                    action: viewModel.loadDashboard,
                    isFoodTabShown: $foodTabShown
                )
                .padding()
                Spacer()
                DatePicker("Datum", selection: $viewModel.date, displayedComponents: .date)
                    .labelsHidden()
                Spacer()
                MinusButton(isDeletePresented: $isDeleteFoodPresented)
                    .padding()
            }
            .padding()
            LazyVStack(spacing: 20) {
                ForEach(viewModel.mealList, id: \.self) { meal in
                    MealNameRow(meal: meal)
                        .padding([.leading, .trailing])
                    let list = viewModel.foodMealList.filter { $0.meal.name == meal.name }
                    if list.isEmpty {
                        Text("Žádné jídlo")
                            .foregroundColor(Color.gray)
                    }
                    else {
                        ForEach(list, id: \.id) { userMealFood in
                            HStack {
                                FoodRecordView(
                                    userMealFood: userMealFood,
                                    currentMealFood: $currentMealFood,
                                    isPresented:     $isInfoPresented
                                )
                                if isDeleteFoodPresented {
                                    Button(action: { viewModel.deleteUserMealRecord(userMeal: userMealFood) } ) {
                                        ZStack {
                                            Circle()
                                                .foregroundColor(.white)
                                                .frame(width: 20, height: 20 )
                                            Image(systemName: "xmark.circle.fill")
                                                .resizable()
                                                .frame(width: 20, height: 20)
                                                .foregroundColor(Color.red)
                                        }
                                        .frame(width:20, height: 20)
                                        .padding(.trailing)
                                    }
                                }
                            }
                            .padding(.horizontal)
                        }
                    } // nested ForEach end
                } // outer ForEach end
            }
        }
    }

}
