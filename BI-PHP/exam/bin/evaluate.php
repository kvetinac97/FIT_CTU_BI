#!/usr/bin/env php
<?php

$tasks = [
    [
        'name' => 'Triangle',
        'subtasks' => [
            [
                'name' => 'Basics',
                'testsuite' => 'FinalTask\Tests\Triangle\TriangleBasicsTest',
                'points' => 3,
            ],
            [
                'name' => 'Special',
                'testsuite' => 'FinalTask\Tests\Triangle\TriangleSpecialTest',
                'points' => 3
            ],
            [
                'name' => 'Similar',
                'testsuite' => 'FinalTask\Tests\Triangle\TriangleSimilarTest',
                'points' => 4
            ]
        ]
    ], [
        'name' => 'Covid',
        'subtasks' => [
            [
                'name' => 'InvalidInput',
                'testsuite' => 'FinalTask\Tests\Covid\InvalidInputTest',
                'points' => 3
            ], [
                'name' => 'CorrectInput',
                'testsuite' => 'FinalTask\Tests\Covid\CorrectInputTest',
                'points' => 7
            ]
        ]
    ], [
        'name' => 'Events',
        'subtasks' => [
            [
                'name' => 'CreateDbTable',
                'testsuite' => 'FinalTask\Tests\Events\CreateDbTableTest',
                'points' => 1
            ], [
                'name' => 'Save',
                'testsuite' => 'FinalTask\Tests\Events\SaveTest',
                'points' => 3
            ], [
                'name' => 'FindById',
                'testsuite' => 'FinalTask\Tests\Events\FindByIdTest',
                'points' => 3
            ], [
                'name' => 'FindByVenueAndDate',
                'testsuite' => 'FinalTask\Tests\Events\FindByVenueAndDateTest',
                'points' => 3
            ],
        ]
    ]
];


$xml = simplexml_load_file('results.xml');

$testsuites = $xml->xpath('//testsuite');

$results = [];

foreach ($testsuites as $testsuite) {
    $results[(string)$testsuite['name']] = [
        'errors' => (int)$testsuite['errors'],
        'failures' => (int)$testsuite['failures']
    ];
}


function printResult($label, $points, $maxPoints, $fill=' ') {
    $res = $label;
    for ($i = strlen($label); $i < 23; $i++) {
        $res .= $fill;
    }
    $res .= "$points/$maxPoints";
    echo "$res\n";
}

echo "\n--- RESULTS --------------\n";

$points = 0;
$maxPoints = 0;

foreach ($tasks as $task) {
    $taskPoints = 0;
    $taskMaxPoints = 0;

    $reports = [];

    foreach ($task['subtasks'] as $subtask) {
        $taskMaxPoints += $subtask['points'];
        $result = $results[$subtask['testsuite']];
        $report = [
            'name' => $subtask['name'],
            'maxPoints' => $subtask['points']
        ];

        if ($result['errors'] === 0 && $result['failures'] === 0) {
            $taskPoints += $subtask['points'];
            $report['points'] = $subtask['points'];
        } else {
            $report['points'] = 0;
        }

        $reports[] = $report;
    }

    printResult($task['name'], $taskPoints, $taskMaxPoints, '.');

    foreach ($reports as $report) {
        printResult('- ' . $report['name'], $report['points'], $report['maxPoints']);
    }

    $points += $taskPoints;
    $maxPoints += $taskMaxPoints;
}

$pctg = round(100 * $points / $maxPoints);
echo "\nTOTAL POINTS\t\t$points/$maxPoints [$pctg%]\n";
