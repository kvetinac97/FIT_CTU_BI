<?php declare(strict_types=1);


namespace App;

use App\Invoice\BusinessEntity;
use Dompdf\Dompdf;

class Renderer extends Dompdf
{
    public function makeInvoice(Invoice $invoice): string
    {
        // Init HTML
        $params = ['%DOKLAD_ID' => $invoice->getNumber()];

        // Init supplier & customer
        /** @var BusinessEntity $val */
        foreach ( ['DODAVATEL' => $invoice->getSupplier(), 'ODBERATEL' => $invoice->getCustomer()] as $key => $val ) {
            $params = array_merge ( $params, [
                "%$key"."_NAME" => $val->getName(),
                "%$key"."_STREET" => $val->getAddress()->getStreet(),
                "%$key"."_NUMBER" => $val->getAddress()->getNumber(),
                "%$key"."_ZIP" => preg_replace("/^(\d\d\d)(\d\d)$/", "$1 $2", $val->getAddress()->getZipCode()),
                "%$key"."_CITY" => $val->getAddress()->getCity(),
                "%$key"."_ICO" => $val->getVatNumber(),
                "%$key"."_PHONE" => $val->getAddress()->getPhone(),
                "%$key"."_EMAIL" => $val->getAddress()->getEmail(),
            ]);
        }

        // Init items
        $invoiceItems = "";
        foreach ( $invoice->getItems() as $item ) {
            $invoiceItems .= "<tr class=\"bordered\"><td class=\"borderedFirst\">{$item->getDescription()}</td><td class=\"bordered\">{$item->getQuantity()}</td><td class=\"bordered\">" .
            number_format($item->getUnitPrice(), 2, ',', ' ') . "</td><td class=\"bordered\">" .
            number_format($item->getTotalPrice(), 2, ',', ' ') . "</td></tr>\n";
        }

        // Finish off
        $params["%ITEM_ROWS"] = $invoiceItems;
        $params["%TOTAL_PRICE"] = number_format($invoice->getTotalPrice(), 2, ',', ' ');
        $html = str_replace( array_keys($params), array_values($params), file_get_contents("template.html") );

        // Create PDF and render
        $dompdf = new Dompdf();
        $dompdf->loadHtml($html);
        $dompdf->setPaper('A4', 'portrait');
        $dompdf->render();
        return $dompdf->output();
    }
}
