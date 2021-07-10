//
//  KitchenView.swift
//  RestSys
//
//  Created by Thats Me on 22.01.2021.
//

import SwiftUI

struct KitchenView: View {
    
    @EnvironmentObject var user: AuthUser
    @StateObject var viewModel = KitchenViewModel()
    
    var body: some View {
        ZStack {
            ScrollView {
                LazyVGrid(columns: [GridItem(.adaptive(minimum: 240), alignment: .top)]) {
                    ForEach(viewModel.orderList.filter({viewModel.history ? $0.done.count > 1 : $0.food.count > 1}).sorted(by: {viewModel.history ? $0.timestamp > $1.timestamp : $0.timestamp < $1.timestamp})) { kitchenItem in
                        KitchenItemView(item: kitchenItem, history: viewModel.history, action: itemTap)
                    }
                    .padding([.bottom])
                }
                .padding([.top])
            }
            
            if viewModel.orderList.filter({viewModel.history ? $0.done.count > 1 : $0.food.count > 1}).isEmpty {
                Text(viewModel.history ? "No history" : "No orders left!")
            }
        }
        .navigationTitle(Text("Kitchen"))
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarItems(
            leading: Button(action: { viewModel.history.toggle() }) {
                Image(systemName: viewModel.history ? "clock.fill" : "clock")
            },
            trailing: Button(action: user.logout) {
                Image(systemName: "lock")
            }
        )
    }
    
}

extension KitchenView {
    
    func itemTap (item: KitchenItem, food: String) {
        viewModel.cookedFood(item: item, food: food)
    }
    
}

struct KitchenView_Previews: PreviewProvider {
    static var previews: some View {
        KitchenView()
            .environmentObject(AuthUser())
    }
}
