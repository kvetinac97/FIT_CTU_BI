//
//  AddFoodView.swift
//  CalTrack
//
//  Created by Roman Isaev on 21/04/2021.
//

import SwiftUI

struct AddFoodView: View {
    @StateObject var viewModel = AddFoodViewModel()
    @State var food: Food
    
    @Binding var sheetPresented: Bool
    let action: () -> ()
    
    var body: some View {
        VStack {
            if viewModel.state == .busy {
                ProgressView()
            }
            else {
                Form {
                    HStack {
                        Text("Množství:")
                            .font(.headline)
                        
                        TextField("2", text: $viewModel.amount)
                            .keyboardType(.numberPad)
                            .multilineTextAlignment(.trailing)
                        
                        Text(food.unit)
                    }
                    
                    HStack {
                        Text("Den konzumace:")
                            .font(.headline)
                        
                        Spacer()
                        
                        DatePicker("Datum", selection: $viewModel.date, displayedComponents: .date)
                            .labelsHidden()
                    }
                    
                    HStack {
                        if viewModel.error {
                            Text("Při načítání seznamu chodů nastala chyba")
                            Button("Načíst znovu") {
                                viewModel.loadMealList()
                            }
                        }
                        else if !viewModel.ready {
                            ProgressView()
                                .onAppear {
                                    viewModel.loadMealList()
                                }
                        }
                        else {
                            Text("Chod:")
                                .font(.headline)
                            
                            Spacer()
                            
                            Picker("", selection: $viewModel.mealId) {
                                ForEach(viewModel.mealList) { meal in
                                    Text(meal.name)
                                }
                            }
                        }
                    }
                }
                
                Button(action: { viewModel.createFood(foodId: food.id) }) {
                    Text("Zapsat")
                        .foregroundColor(Color.white)
                        .bold()
                }
                .frame(width: 140, height: 50, alignment: .center)
                .background(LinearGradient(gradient: Gradient(colors: [.white,.blue, .blue]), startPoint: .topLeading, endPoint: .bottomLeading))
                .foregroundColor(Color.white)
                .cornerRadius(30)
                .padding(.bottom, 30)
            }
        }
        .navigationBarTitle("Zapsat \(food.name)", displayMode: .inline)
        .alert(item: $viewModel.errorMessage) { _ in
            Alert(
                title: Text("Error"),
                message: Text(viewModel.errorMessage!.localizedDescription),
                dismissButton: .cancel()
            )
        }
        .onReceive(viewModel.$state) { state in
            switch state {
            case .fresh:
                break
            case .busy:
                break
            case let .done(success):
                if success {
                    sheetPresented = false
                    action()
                }
            }
        }
    }
}
