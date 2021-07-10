//
//  PDFViewController.swift
//  RestSys
//
//  Created by Thats Me on 22.01.2021.
//

import UIKit
import SwiftUI

struct PDFViewController: UIViewControllerRepresentable {

    let viewModel: TableDetailViewModel

    func makeUIViewController (context: UIViewControllerRepresentableContext<PDFViewController>) -> UIActivityViewController {
        let vc = UIActivityViewController(
          activityItems: [createPDF()],
          applicationActivities: []
        )
        return vc
    }
    
    func createPDF () -> Data {
        let pdfMetaData = [
            kCGPDFContextCreator: "RestSys",
            kCGPDFContextAuthor: "Ondrej Wrzecionko",
            kCGPDFContextTitle: "Receipt n. \(viewModel.entities.first?.order ?? 0)"
        ]
        
        let format = UIGraphicsPDFRendererFormat()
        format.documentInfo = pdfMetaData as [String: Any]

        // Render the view behind all other views
        let rootVC = UIApplication.shared.windows.first?.rootViewController

        // Render the PDF
        let pdfRenderer = UIGraphicsPDFRenderer(bounds: CGRect(x: 0, y: 0, width: 595, height: 842), format: format)

        let data = pdfRenderer.pdfData(actions: { (context) in
            let pages = Int(ceil(Double(viewModel.entities.count) / Double(PDFView.PAGE_SIZE)))
            for i in 1 ... pages {
                let pdfVC = UIHostingController(rootView: PDFView(viewModel: viewModel, page: i, pages: pages))
                pdfVC.view.frame = CGRect(x: 0, y: 0, width: 595, height: 842)
                
                rootVC?.addChild(pdfVC)
                rootVC?.view.insertSubview(pdfVC.view, at: 0)
                
                context.beginPage()
                pdfVC.view.layer.render(in: context.cgContext)
                
                pdfVC.removeFromParent()
                pdfVC.view.removeFromSuperview()
            }
        })
        
        return data
    }

    func updateUIViewController(_ uiViewController: UIActivityViewController, context: UIViewControllerRepresentableContext<PDFViewController>) {}

}
