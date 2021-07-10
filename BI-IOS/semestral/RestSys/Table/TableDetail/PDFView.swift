//
//  PDFView.swift
//  RestSys
//
//  Created by Thats Me on 22.01.2021.
//

import SwiftUI

struct PDFView: View {
    
    let viewModel: TableDetailViewModel
    let page: Int
    let pages: Int
    
    static let PAGE_SIZE = 15
    
    var body: some View {
        VStack {
            Text("Order #\(viewModel.entities.first?.order ?? 0)")
                .font(.largeTitle)
                .fontWeight(.bold)
                .padding([.top])
            
            List {
                ForEach(viewModel.entities.suffix(from: (page - 1) * PDFView.PAGE_SIZE).prefix(PDFView.PAGE_SIZE)) { foodOrder in
                    FoodOrderItemView(foodOrder: foodOrder, decrementAction: nil)
                        .padding([.top, .bottom], 4)
                }
                
                // Last page
                if page == pages {
                    HStack {
                        Text("Total:")
                            .font(.headline)
                        Spacer()
                        Text(String(viewModel.entities.map({$0.count * $0.food.price}).reduce(0, +)) + ",-")
                    }
                }
            }
            .padding([.top])
            
            Spacer()
            Divider()
            
            HStack {
                Text("Page \(page)/\(pages)")
                Spacer()
                Text("Printed on \(DateFormatter.init(format: "YYYY.MM.dd HH:mm").string(from: Date.init()))")
            }
            .padding()
        }
    }
    
}

struct PDFView_Previews: PreviewProvider {
    static var previews: some View {
        PDFView(viewModel: TableDetailViewModel(), page: 1, pages: 1)
    }
}
