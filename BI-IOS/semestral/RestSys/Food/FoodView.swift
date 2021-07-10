//
//  FoodView.swift
//  RestSys
//
//  Created by Thats Me on 26.12.2020.
//

import SwiftUI
import SwiftUIRefresh

struct FoodView: View {
    
    @StateObject var foodList = FoodViewModel()
    @EnvironmentObject var user: AuthUser
    
    @State private var isShowing = false
    @State var createFoodPresented = false
    
    var body: some View {
        CRUDView<Food, List>(viewModel: foodList, reload: foodList.loadFood) {
            List {
                ForEach(foodList.entities) { foodItem in
                    FoodItemView(foodItem: foodItem, reload: foodList.loadFood)
                        .padding([.top, .bottom], 4)
                }
                .onDelete(perform: { indexSet in
                    if user.admin {
                        foodList.removeFood(set: indexSet)
                    }
                })
            }
        }
        .navigationTitle(Text(user.loggedIn ? "Food" : "Food list"))
        .navigationBarTitleDisplayMode(user.loggedIn ? .inline : .automatic)
        .navigationBarItems(
            leading: user.admin ? AnyView(EditButton()) : AnyView(EmptyView()),
            trailing: user.admin ? AnyView(Button(action: showCreateFood) { Image(systemName: "plus") }) : AnyView(EmptyView())
        )
        .sheet(isPresented: $createFoodPresented, onDismiss: foodList.loadFood) {
            NavigationView {
                FoodCreateView(viewModel: FoodCreateViewModel())
            }
        }
    }
}

extension FoodView {
    func showCreateFood () {
        createFoodPresented = true
    }
}

struct FoodView_Previews: PreviewProvider {
    static var previews: some View {
        FoodView()
    }
}
