//
//  FoodSheet.swift
//  CalTrack
//
//  Created by Roman Isaev on 20/04/2021.
//

import SwiftUI

struct FoodSheet: View {
    @StateObject var viewModel = FoodViewModel()
    @Binding var presented: Bool
    let action: () -> ()
    
    var body: some View {
        NavigationView {
            ZStack (alignment: .leading) {
                Color(UIColor.darkGray)
                    .edgesIgnoringSafeArea(.all)
                
                VStack {
                    HStack {
                        Image(systemName: "magnifyingglass.circle")
                            .resizable()
                            .frame(width: 25, height: 25)
                            .foregroundColor(.blue)
                            .overlay(
                                Circle()
                                    .stroke(LinearGradient(gradient: Gradient(colors: [.white,.blue, .blue]), startPoint: .topLeading, endPoint: .bottomLeading), lineWidth: 1.2)
                                    .frame(width: 25, height: 25) )
                            .padding()
                        ZStack {
                            if viewModel.searchName.isEmpty {
                                HStack {
                                    Text("Hledat jídlo...")
                                        .foregroundColor(.black)
                                        .padding()
                                    Spacer()
                                }
                            }
                            TextField("", text: $viewModel.searchName)
                                .font(Font.system(size: 20, design: .default))
                                .foregroundColor(.black)
                                .padding()
                        }
                    }
                    .background(Color.white)
                    .cornerRadius(40)
                    .padding()
                    
                    ZStack {
                        if viewModel.error {
                            VStack {
                                Text("Při načítání nastala chyba")
                                Button("Zkusit znovu") {
                                    viewModel.loadList()
                                }
                            }
                        }
                        else if !viewModel.ready {
                            ProgressView()
                                .onAppear {
                                    viewModel.loadList()
                                }
                        }
                        else {
                            LazyVStack() {
                                ForEach(viewModel.foodList) { food in
                                    NavigationLink(
                                        destination:
                                            AddFoodView(
                                                food: food,
                                                sheetPresented: $presented,
                                                action: action
                                            )
                                    ) {
                                        FoodView(
                                            food: food,
                                            foodCalories:
                                                viewModel
                                                .caloryService
                                                .caloriesFromMacronutrients(
                                                    protein: food.protein,
                                                    carbohydrates: food.carbohydrates,
                                                    fat: food.fat
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    .padding()
                    Spacer()
                }
            }
            .navigationBarTitle("Vyhledávání jídla", displayMode: .inline)
        }
    }
}

struct FoodSheet_Previews: PreviewProvider {
    static var previews: some View {
        FoodSheet(presented: .constant(false)) {}
    }
}
