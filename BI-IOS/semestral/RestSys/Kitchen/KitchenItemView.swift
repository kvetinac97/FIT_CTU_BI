//
//  KitchenItemView.swift
//  RestSys
//
//  Created by Thats Me on 22.01.2021.
//

import SwiftUI

struct KitchenItemView: View {
    
    let item: KitchenItem
    let history: Bool
    let action: (KitchenItem, String) -> ()
    
    var body: some View {
        VStack {
            Text(item.table + " (" + String(Date.init() - item.timestamp) + " minutes)")
                .font(.headline)
            Divider()
            
            ForEach((history ? item.done : item.food).filter({$0 != "Dummy"}), id: \.self) { food in
                HStack {
                    Text(food)
                        .padding([.leading])
                    Spacer()
                    Button(action: { action(item, food) }) {
                        Image(systemName: history ? "plus.circle" : "minus.circle")
                    }
                    .padding([.trailing])
                }
                Divider()
            }
        }
    }
    
}

struct KitchenItemView_Previews: PreviewProvider {
    static var previews: some View {
        KitchenItemView(item: KitchenItem(table: "Inside 1", food: ["Kebab", "Kebab", "Pivo"], done: [], timestamp: Date.init()), history: false, action: {(_, _) in })
    }
}
