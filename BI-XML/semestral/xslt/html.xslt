<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes" encoding="utf-8" media-type="text/html" doctype-public="-//W3C//DTD HTML 4.0//EN"/>
    <xsl:param name="num-format">US</xsl:param>
    <xsl:decimal-format name="US" />
    <xsl:variable name="num-pattern">#,###</xsl:variable>

    <xsl:template match="/">
        <html lang="en">
            <head>
                <title>Countries</title>
                <link href="style.css" rel="stylesheet" />
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous" />
            </head>
            <body class="d-flex flex-column h-100 min-vh-100">
                <header>
                    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
                        <a class="navbar-brand">BI-XML</a>
                        <button class="navbar-toggler" type="button" data-toggle="collapse"
                                data-target="#navbarCollapse" aria-controls="navbarCollapse"
                                aria-expanded="false" aria-label="Toggle navigation">
                            <span class="navbar-toggler-icon" />
                        </button>
                        <div class="collapse navbar-collapse" id="navbarCollapse">
                            <ul class="navbar-nav mr-auto">
                                <li class="nav-item active"><a class="nav-link">Home</a></li>
                                <xsl:for-each select="/countries/country">
                                    <li class="nav-item"><a class="nav-link" href="{concat(@short, '.html')}"><xsl:value-of select="@name" /></a></li>
                                </xsl:for-each>
                            </ul>
                        </div>
                    </nav>
                </header>

                <main role="main" class="flex-shrink-0">
                    <div class="container">
                        <h1 class="mt-5">Countries:</h1>
                        <ul class="lead">
                            <xsl:for-each select="/countries/country">
                                <li><a href="{concat(@short, '.html')}"><xsl:value-of select="@name" /></a></li>
                            </xsl:for-each>
                        </ul>
                    </div>
                </main>

                <footer class="footer mt-auto py-3">
                    <div class="container">
                        <span class="text-muted">(c) Ondřej Wrzecionko 2020</span>
                    </div>
                </footer>

                <xsl:apply-templates select="node()" />
                <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous" />
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous" />
            </body>
        </html>
    </xsl:template>

    <xsl:template match="country">
        <xsl:result-document href="{concat(@short, '.html')}">
        <html lang="en">
            <head>
                <title><xsl:value-of select="@name" /></title>
                <link href="style.css" rel="stylesheet" />
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous" />
            </head>
            <body class="d-flex flex-column h-100 min-vh-100">
                <header>
                    <nav class="navbar navbar-expand navbar-dark fixed-top bg-dark">
                        <a class="navbar-brand"><xsl:value-of select="@name" /></a>
                        <button class="navbar-toggler" type="button" data-toggle="collapse"
                                data-target="#navbarCollapse" aria-controls="navbarCollapse"
                                aria-expanded="false" aria-label="Toggle navigation">
                            <span class="navbar-toggler-icon" />
                        </button>
                        <div class="collapse navbar-collapse" id="navbarCollapse">
                            <ul class="nav navbar-nav mr-auto" role="tablist">
                                <li class="nav-item"><a class="nav-link" href="output.html">Home</a></li>
                                <xsl:for-each select="node()">
                                    <li class="nav-item" role="presentation">
                                        <a class="{concat('nav-link ', replace(name(), 'introduction', 'active'))}" id="label-{name()}" data-toggle="tab" href="{concat('#', name())}" role="tab">
                                            <xsl:choose>
                                                <xsl:when test="string-length(substring-before(name(), '_')) != 0">
                                                    <xsl:value-of select="concat(upper-case(substring(name(), 1, 1)), substring-before(substring(name(), 2), '_'))" />
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:value-of select="concat(upper-case(substring(name(), 1, 1)), substring(name(), 2))" />
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </a>
                                    </li>
                                </xsl:for-each>
                            </ul>
                        </div>
                    </nav>
                </header>

                <main role="main" class="flex-shrink-0">
                    <div class="container tab-content">
                        <xsl:apply-templates select="node()" />
                    </div>
                </main>

                <footer class="footer mt-auto py-3">
                    <div class="container">
                        <span class="text-muted">(c) Ondřej Wrzecionko 2020</span>
                    </div>
                </footer>

                <!-- Map, chart scripts -->
                <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.3.0-rc.1/Chart.js" type="text/javascript"  />
                <script src="https://api.mapy.cz/loader.js" type="text/javascript" />
                <script src="script.js" type="text/javascript" />

                <!-- Style scripts -->
                <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous" />
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous" />
            </body>
        </html>
        </xsl:result-document>
    </xsl:template>

    <xsl:template match="introduction">

        <div class="card tab-pane fade show active" id="introduction" role="tabpanel">
            <h5 class="card-header">Introduction</h5>
            <div class="card-body">
                <xsl:for-each select="paragraph">
                    <p><xsl:value-of select="."/></p>
                </xsl:for-each>
            </div>
        </div>

    </xsl:template>

    <xsl:template match="geography">

        <div class="card tab-pane fade" id="geography" role="tabpanel">
            <h5 class="card-header">Geography</h5>
            <div class="card-body">
                <div class="row">
                    <div class="col-6">

                        <xsl:apply-templates select="span[@align='top']" />

                        <!-- Area -->
                        <xsl:for-each select="area/@*">
                            <xsl:choose>
                                <xsl:when test="name() = 'rank'">
                                    <br />
                                    <b>Area ranking: </b><xsl:value-of select="." /> in the world <br />
                                </xsl:when>
                                <xsl:otherwise>
                                    <b><xsl:value-of select="concat('Land - ', name())" />: </b> <xsl:value-of select="format-number(., $num-pattern, $num-format)" /> km<sup>2</sup> <br />
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:for-each>
                        <b>Area comparative: </b><xsl:value-of select="area"/> <br /> <br />

                        <b>Boundaries: </b> (<xsl:value-of select="boundaries/@total" /> km with <xsl:value-of select="boundaries/@count" /> countries)
                        <ul>
                            <xsl:for-each select="boundaries/boundary">
                                <li><xsl:value-of select="@with" /> (<xsl:value-of select="@length" /> km) </li>
                            </xsl:for-each>
                        </ul>

                        <b>Maritime claims: </b> <xsl:if test="not(maritime_claims/maritime_claim)">none</xsl:if>
                        <ul>
                            <xsl:for-each select="maritime_claims/maritime_claim">
                                <li>
                                    <b>Type: </b> <xsl:value-of select="@type" />,
                                    <b>Length: </b> <xsl:value-of select="." /> km
                                </li>
                            </xsl:for-each>
                        </ul>

                        <b>Elevation: </b>
                        <ul>
                            <li><b>Mean: </b> <xsl:value-of select="elevation/@mean" /> a.s.l.</li>
                            <li><b>Lowest: </b> <xsl:value-of select="elevation/@lowestalt" /> a.s.l. (<xsl:value-of select="elevation/@lowest" />)</li>
                            <li><b>Highest: </b> <xsl:value-of select="elevation/@highestalt" /> a.s.l. (<xsl:value-of select="elevation/@highest" />)</li>
                        </ul>

                        <!-- Land -->
                        <b>Irrigated land: </b> <xsl:value-of select="land/@irrigated" /> km<sup>2</sup> <br /> <br />

                        <b>Land: </b>
                        <ul>
                            <li>
                                <b>Agricultural: </b> <xsl:value-of select="land/@agricultural" /> %
                                <ul>
                                    <li><b>Arable: </b> <xsl:value-of select="land/@arable" /> %</li>
                                    <li><b>Pasture: </b> <xsl:value-of select="land/@arablepasture" /> %</li>
                                    <li><b>Crops: </b> <xsl:value-of select="land/@arablecrops" /> %</li>
                                </ul>
                            </li>
                            <li><b>Forest: </b> <xsl:value-of select="land/@forest" /> %</li>
                            <li><b>Other: </b> <xsl:value-of select="land/@other" /> %</li>
                        </ul>

                        <xsl:apply-templates select="span[not(@align)]" />

                    </div>
                    <div class="col-6" style="float: right">
                        <div id="m" style="width: 100%; height:600px">
                            <noscript>Javascript unavailable, couldn't load Map</noscript>
                        </div> <br /> <br />

                        <h3 style="text-align: center">Land division chart: </h3>
                        <canvas id="landChart" style="width: 100%; height: 600px">
                            <noscript>Javascript unavailable, couldn't load chart</noscript>
                        </canvas> <br /> <br />

                        <xsl:apply-templates select="span[@align='right']" />
                    </div>
                </div>

                <!-- Script variables -->
                <script type="text/javascript">
                    let lat = <xsl:value-of select="coordinates/@latitude" />;
                    let latType = '<xsl:value-of select="coordinates/@lattype" />';
                    let long = <xsl:value-of select="coordinates/@longitude" />;
                    let longType = '<xsl:value-of select="coordinates/@longtype" />';

                    let agriculturalLand = <xsl:value-of select="land/@agricultural" />;
                    let forestLand = <xsl:value-of select="land/@forest" />;
                    let otherLand = <xsl:value-of select="land/@other" />;
                </script>
            </div>
        </div>

    </xsl:template>

    <xsl:template match="people_and_society">

        <div class="card tab-pane fade" id="people_and_society" role="tabpanel">
            <h5 class="card-header">People and society</h5>
            <div class="card-body">
                <div class="row">
                    <div class="col-6">
                        <b>Population: </b> <xsl:value-of select="format-number(population/@count, $num-pattern, $num-format)" /> (ranking: <xsl:value-of select="population/@ranking" />)<br/>
                        <b>Nationality: </b> <xsl:value-of select="nationality/@noun" /> (adjective: <xsl:value-of select="nationality/@adjective" />)<br/>
                        <b>Ethnic groups: </b> <xsl:value-of select="ethnic_groups" /><br/><br/>

                        <span id="langTextDiv">
                            <b>Languages:</b>
                            <ul>
                                <xsl:for-each select="languages/language">
                                    <li>
                                        <xsl:choose>
                                            <xsl:when test="@official"><b><xsl:value-of select="@name" /></b></xsl:when>
                                            <xsl:otherwise><xsl:value-of select="@name" /></xsl:otherwise>
                                        </xsl:choose>

                                        <xsl:if test="@percent">
                                            (<xsl:value-of select="@percent" /> %)
                                        </xsl:if>
                                    </li>
                                </xsl:for-each>
                            </ul>
                        </span>

                        <b>Religions:</b>
                        <ul>
                            <xsl:for-each select="religions/religion">
                                <li>
                                    <b><xsl:value-of select="@name"/></b>: <xsl:value-of select="@percent" /> %
                                </li>
                            </xsl:for-each>
                        </ul>

                        <b>Age structure:</b>
                        <ul>
                            <xsl:for-each select="age_structure/age">
                                <li>
                                    <b><xsl:value-of select="@min"/>-<xsl:value-of select="@max"/> years</b>: <xsl:value-of select="@percent" /> %
                                    (male <xsl:value-of select="format-number(@male, $num-pattern, $num-format)" /> / female <xsl:value-of select="format-number(@female, $num-pattern, $num-format)" /> )
                                </li>
                            </xsl:for-each>
                        </ul>

                        <xsl:for-each select="dependency_ratio/@*">
                            <b><xsl:value-of select="concat(upper-case(substring(name(),1,1)), substring(name(), 2))" /> dependency ratio: </b> <xsl:value-of select="." /> <br />
                        </xsl:for-each>
                        <br />

                        <xsl:for-each select="rate_per_thousand/@*">
                            <b><xsl:value-of select="concat(upper-case(substring(name(),1,1)), substring(name(), 2))" /> rate: </b> <xsl:value-of select="." /> <xsl:value-of select="concat(' ', name())" />s / 1,000 population <br />
                        </xsl:for-each>
                        <br />

                        <b>Sex ratio:</b>
                        <ul>
                            <li><b>Total: </b> <xsl:value-of select="sex_ratio/@total" /> male(s)/female </li>
                            <xsl:for-each select="sex_ratio/age">
                                <li>
                                    <b><xsl:value-of select="@min"/>-<xsl:value-of select="@max"/> years</b>: <xsl:value-of select="@male_over_female" /> male(s)/female
                                </li>
                            </xsl:for-each>
                        </ul>

                        <b>Mother's mean age at first birth: </b> <xsl:value-of select="average_mother/@first_birth_age" /> years <br />
                        <b>Maternal mortality: </b> <xsl:value-of select="average_mother/@mortality_rate" /> deaths / 100,000 live births <br />
                        <b>Total fertility rate: </b> <xsl:value-of select="average_mother/@children" /> children / woman <br /> <br />

                        <b>Drinking water source access: </b>
                        <ul>
                            <xsl:for-each select="drinking_water/@*">
                                <li><xsl:value-of select="name()" />: <xsl:value-of select="." /> % of population </li>
                            </xsl:for-each>
                        </ul>

                        <b>Sanitation facility access: </b>
                        <ul>
                            <xsl:for-each select="sanitation_facility_access/@*">
                                <li><xsl:value-of select="name()" />: <xsl:value-of select="." /> % of population </li>
                            </xsl:for-each>
                        </ul>

                        <b>Diseases: </b> <xsl:if test="not(diseases/disease)">none</xsl:if>
                        <ul>
                            <xsl:for-each select="diseases/disease">
                                <li><b><xsl:value-of select="@type"/> disease(s)</b>: <xsl:value-of select="."/></li>
                            </xsl:for-each>
                        </ul>
                    </div>
                    <div class="col-6" style="float: right">
                        <div id="langChartDiv">
                            <h3 style="text-align: center">Language division chart: </h3>
                            <canvas id="langChart" style="width: 100%; height: 600px">
                                <noscript>Javascript unavailable, couldn't load chart</noscript>
                            </canvas>

                            <br /><br /><br />
                        </div>

                        <b>Health expenditure: </b> <xsl:value-of select="health_expenditure" /> <br />

                        <xsl:for-each select="statistics/statistic">
                            <b><xsl:value-of select="." />: </b>
                            <ul>
                                <li>total: <xsl:value-of select="@total" /> % </li>
                                <li>male: <xsl:value-of select="@male" /> % </li>
                                <li>female: <xsl:value-of select="@female" /> % </li>
                                <xsl:if test="@ranking">
                                    <li>ranking: <xsl:value-of select="@ranking" /> </li>
                                </xsl:if>
                            </ul>
                        </xsl:for-each>
                    </div>
                </div>

                <!-- Script variables -->
                <script type="text/javascript">
                    let languages = new Map();
                    <xsl:for-each select="languages/language">
                        <xsl:if test="@percent">
                            languages.set("<xsl:value-of select="@name" />", <xsl:value-of select="@percent" />);
                        </xsl:if>
                    </xsl:for-each>
                </script>
            </div>
        </div>

    </xsl:template>

    <xsl:template match="government">

        <div class="card tab-pane fade" id="government" role="tabpanel">
            <h5 class="card-header">Government</h5>
            <div class="card-body">
                <div class="row">
                    <div class="col-6">
                        <xsl:apply-templates select="span[@align='top']" />

                        <b>Administrative division: </b> <xsl:value-of select="divisions/@count" /><xsl:value-of select="concat(' ', divisions/@name)" />s <br />

                        <b>Citizenship: </b>
                        <ul>
                            <xsl:for-each select="citizenships/citizenship">
                                <li><b>by <xsl:value-of select="@by" />: </b> <xsl:value-of select="." /></li>
                            </xsl:for-each>
                        </ul>

                        <b>Executive power: </b>
                        <ul>
                            <xsl:for-each select="executive/span">
                                <li><xsl:apply-templates select="." /></li>
                            </xsl:for-each>
                        </ul>

                        <b>Legislative power: </b>
                        <ul>
                            <xsl:for-each select="legislative/span">
                                <li><xsl:apply-templates select="." /></li>
                            </xsl:for-each>
                        </ul>
                    </div>
                    <div class="col-6" style="float: right">
                        <xsl:variable name="image_path" select="flag/image" />
                        <img src="{$image_path}" alt="Flag" style="width: 100%; height: auto" /> <br /> <br />

                        <b>Judicial power: </b>
                        <ul>
                            <xsl:for-each select="judicial/span">
                                <li><xsl:apply-templates select="." /></li>
                            </xsl:for-each>
                        </ul>

                        <b>Flag: </b> <xsl:value-of select="flag/description" /> <br /> <br />

                        <xsl:apply-templates select="span[@align='right']" />
                    </div>
                </div>
            </div>
        </div>

    </xsl:template>

    <xsl:template match="economy">

        <div class="card tab-pane fade" id="economy" role="tabpanel">
            <h5 class="card-header">Economy</h5>
            <div class="card-body">
                <div class="row">
                    <div class="col-12">
                        <xsl:apply-templates select="span[@align='top']" />
                        <br /> <br />
                    </div>
                    <div class="col-6">
                        <xsl:apply-templates select="gdp_values/gdp_value" />
                        <br />

                        <xsl:apply-templates select="rates/rate" />
                        <br />

                        <xsl:for-each select="compositions/factor">
                            <b>GDP composition</b> (by <xsl:value-of select="@name"/>) :
                            <ul>
                                <xsl:for-each select="composition">
                                    <li>
                                        <b><xsl:value-of select="replace(concat(upper-case(substring(@for,1,1)), substring(@for, 2)), '_', ' ')"/></b>: <xsl:value-of select="." /> %
                                    </li>
                                </xsl:for-each>
                            </ul>
                        </xsl:for-each>

                        <b>Occupations</b> :
                        <ul>
                            <xsl:for-each select="occupations/occupation">
                                <li>
                                    <b><xsl:value-of select="@name"/></b>: <xsl:value-of select="." /> %
                                </li>
                            </xsl:for-each>
                        </ul>
                    </div>
                    <div class="col-6" style="float: right">
                        <h3 style="text-align: center">GDP division by sector of origin: </h3>
                        <canvas id="gdpChart" style="width: 100%; height: 600px">
                            <noscript>Javascript unavailable, couldn't load chart</noscript>
                        </canvas> <br /> <br />

                        <xsl:for-each select="eximports/eximport">
                            <b><xsl:value-of select="@for" /> - <xsl:value-of select="@name" />: </b> <xsl:value-of select="." /> <br />
                        </xsl:for-each>
                        <br />

                        <xsl:apply-templates select="span[@align='right']" />
                    </div>
                </div>

                <!-- Script variables -->
                <script type="text/javascript">
                    let compositions = new Map();
                    <xsl:for-each select="compositions/factor/composition">
                        compositions.set("<xsl:value-of select="@for" />", <xsl:value-of select="." />);
                    </xsl:for-each>
                </script>
            </div>
        </div>

    </xsl:template>
    <xsl:template match="gdp_value">
        <b>GDP - <xsl:value-of select="@type" />: </b> $<xsl:value-of select="." /> billion <xsl:if test="@ranking">(ranking <xsl:value-of select="@ranking" />)</xsl:if> <br />
    </xsl:template>
    <xsl:template match="rate">
        <b><xsl:value-of select="replace(concat(upper-case(substring(@type,1,1)), substring(@type, 2)), '_', ' ')"/></b>: <xsl:value-of select="." /> % <br />
    </xsl:template>

    <xsl:template match="energy">

        <div class="card tab-pane fade" id="energy" role="tabpanel">
            <h5 class="card-header">Energy</h5>
            <div class="card-body">
                <div class="row">
                    <div class="col-6">
                        <b>Electricity - by source:</b>
                        <ul>
                            <xsl:for-each select="electricity_source/@*">
                                <li><b><xsl:value-of select="replace(name(), '_', ' ')"/>: </b> <xsl:value-of select="." /> % </li>
                            </xsl:for-each>
                        </ul>

                        <h3>Resources:</h3>

                        <xsl:for-each select="resource_list/resource">
                            <b><xsl:value-of select="@name" /> </b>:
                            <ul>
                                <xsl:variable name="unit" select="@unit" />
                                <xsl:variable name="runit" select="@runit" />

                                <xsl:for-each select="@*">
                                    <xsl:choose>
                                        <xsl:when test="name() = 'unit' or name() = 'runit' or name() = 'name'" />
                                        <xsl:otherwise>
                                            <li>
                                                <b><xsl:value-of select="name()" />: </b>
                                                <xsl:choose>
                                                    <xsl:when test="name() = 'reserves'">
                                                        <xsl:value-of select="concat(format-number(., $num-pattern, $num-format), ' ', $runit)"/>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:value-of select="concat(format-number(., $num-pattern, $num-format), ' ', $unit)"/>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </li>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:for-each>
                            </ul>
                        </xsl:for-each>
                    </div>
                    <div class="col-6" style="float: right">
                        <h3>Energy production by source: </h3>
                        <canvas id="energyChart" style="width: 100%; height: 600px">
                            <noscript>Javascript unavailable, couldn't load chart</noscript>
                        </canvas> <br />

                        <b>People without electricity: </b> <xsl:value-of select="format-number(electrification/@without, $num-pattern, $num-format)" /> <br />
                        <b>Electricity access: </b>
                        <ul>
                            <xsl:for-each select="electrification/@*">
                                <li><b><xsl:value-of select="replace(concat(upper-case(substring(name(),1,1)), substring(name(), 2)), '_', ' ')" />: </b> <xsl:value-of select="."/> %</li>
                            </xsl:for-each>
                        </ul>

                        <b>Electricity production: </b> <xsl:value-of select="electricity/@production" /> billion kWh (ranking <xsl:value-of select="electricity/@production_rank" />) <br />
                        <b>Electricity consumption: </b> <xsl:value-of select="electricity/@consumption" /> billion kWh (ranking <xsl:value-of select="electricity/@consumption_rank" />) <br />
                        <b>Electricity - export: </b> <xsl:value-of select="electricity/@exports" /> billion kWh <br />
                        <b>Electricity - import: </b> <xsl:value-of select="electricity/@imports" /> billion kWh <br />
                        <b>Installed generating capacity: </b> <xsl:value-of select="electricity/@generating" /> billion kWh <br />
                    </div>
                </div>

                <!-- Script variables -->
                <script type="text/javascript">
                    let fossil = <xsl:value-of select="electricity_source/@fossil" />;
                    let nuclear = <xsl:value-of select="electricity_source/@nuclear" />;
                    let hydroelectric = <xsl:value-of select="electricity_source/@hydroelectric" />;
                    let other = <xsl:value-of select="electricity_source/@other_renewable" />;
                </script>
            </div>
        </div>

    </xsl:template>

    <xsl:template match="communications">

        <div class="card tab-pane fade" id="communications" role="tabpanel">
            <h5 class="card-header">Communications</h5>
            <div class="card-body">
                <div class="row">
                    <div class="col-6">
                        <xsl:for-each select="phones/phone">
                            <b>Telephones - <xsl:value-of select="@type" />: </b>
                            <ul>
                                <li><b>total subscriptions: </b><xsl:value-of select="format-number(@total, $num-pattern, $num-format)"/></li>
                                <li><b>subscriptions per 100 habitants: </b><xsl:value-of select="@per_100"/></li>
                                <li><b>country ranking: </b><xsl:value-of select="@ranking"/></li>
                            </ul>
                        </xsl:for-each>

                        <xsl:apply-templates select="span[@align='right']" />

                        <b>Internet code: </b> .<xsl:value-of select="internet/@code" /> <br />
                        <b>Internet users: </b> <xsl:value-of select="format-number(internet/@users, $num-pattern, $num-format)" /> <br />
                        <b>Percent of population with access to internet: </b> <xsl:value-of select="internet/@percent" /> % (ranking <xsl:value-of select="internet/@ranking" />) <br />
                    </div>
                    <div class="col-6" style="float: right">
                        <b>Phone systems: </b>
                        <ul>
                            <xsl:for-each select="phone_systems/phone_system">
                                <li><b><xsl:value-of select="@type"/>: </b><xsl:value-of select="." /></li>
                            </xsl:for-each>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

    </xsl:template>

    <xsl:template match="transportation">

        <div class="card tab-pane fade" id="transportation" role="tabpanel">
            <h5 class="card-header">Transportation</h5>
            <div class="card-body">
                <div class="row">
                    <div class="col-6">
                        <xsl:apply-templates select="span[@align='top']" />

                        <b>Air transportation: </b>
                        <ul>
                            <xsl:for-each select="air/air_unit">
                                <li><b><xsl:value-of select="@name"/>: </b> <xsl:value-of select="format-number(., $num-pattern, $num-format)" /></li>
                            </xsl:for-each>
                            <xsl:for-each select="air/span">
                                <li><b><xsl:value-of select="@name"/>: </b> <xsl:value-of select="." /></li>
                            </xsl:for-each>
                        </ul>

                        <b>Airports: </b>
                        <ul>
                            <li><b>Total: </b> <xsl:value-of select="air/airports/@total" /> </li>
                            <xsl:for-each select="air/airports/airport">
                                <li><b><xsl:value-of select="@min" /> to <xsl:value-of select="@max" /> m</b>: <xsl:value-of select="." /></li>
                            </xsl:for-each>
                        </ul>
                    </div>
                    <div class="col-6" style="float: right">
                        <b>Rails: </b>
                        <xsl:choose>
                            <xsl:when test="rails/@total=0">
                                none <br />
                            </xsl:when>
                            <xsl:otherwise>
                                <ul>
                                    <li><b>total: </b> <xsl:value-of select="format-number(rails/@total, $num-pattern, $num-format)" /> km </li>
                                    <xsl:if test="rails/@ranking">
                                        <li><b>ranking: </b> <xsl:value-of select="rails/@ranking" /> </li>
                                    </xsl:if>

                                    <xsl:for-each select="rails/rail">
                                        <li><b><xsl:value-of select="@gauge" /> gauge</b>: <xsl:value-of select="format-number(., $num-pattern, $num-format)" /> km </li>
                                    </xsl:for-each>
                                </ul>
                            </xsl:otherwise>
                        </xsl:choose>

                        <b>Roads: </b>
                        <ul>
                            <li><b>Total: </b> <xsl:value-of select="format-number(roads/@total, $num-pattern, $num-format)" /> km </li>
                            <xsl:if test="roads/@ranking">
                                <li><b>ranking: </b> <xsl:value-of select="roads/@ranking" /> </li>
                            </xsl:if>

                            <xsl:for-each select="roads/road">
                                <li><b><xsl:value-of select="@type" /></b>: <xsl:value-of select="format-number(., $num-pattern, $num-format)" /> km </li>
                            </xsl:for-each>
                        </ul>

                        <b>Water: </b>
                        <ul>
                            <li><b>Length: </b> <xsl:value-of select="format-number(water/length, $num-pattern, $num-format)" /> km </li>
                            <xsl:if test="water/ranking">
                                <li><b>Ranking: </b> <xsl:value-of select="water/ranking" /> </li>
                            </xsl:if>
                            <li><b>Ports: </b> <xsl:value-of select="water/ports" /> </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

    </xsl:template>

    <xsl:template match="military_and_security">

        <div class="card tab-pane fade" id="military_and_security" role="tabpanel">
            <h5 class="card-header">Military and security</h5>
            <div class="card-body">
                <div class="row">
                    <div class="col-12">
                        <xsl:apply-templates select="span" />
                    </div>
                </div>
            </div>
        </div>

    </xsl:template>

    <xsl:template match="terrorism">

        <div class="card tab-pane fade" id="terrorism" role="tabpanel">
            <h5 class="card-header">Terrorism</h5>
            <div class="card-body">
                <div class="row">
                    <div class="col-12">
                        <b>Terrorist group(s): </b> <xsl:value-of select="." />
                    </div>
                </div>
            </div>
        </div>

    </xsl:template>

    <xsl:template match="transnational_issues">

        <div class="card tab-pane fade" id="transnational_issues" role="tabpanel">
            <h5 class="card-header">Transnational issues</h5>
            <div class="card-body">
                <div class="row">
                    <div class="col-12">
                        <xsl:apply-templates select="span" />

                        <b>Refugees: </b>
                        <xsl:choose>
                            <xsl:when test="refugees/refugee">
                                <ul>
                                    <xsl:for-each select="refugees/refugee">
                                        <li><b><xsl:value-of select="@from"/></b>: <xsl:value-of select="format-number(@count, $num-pattern, $num-format)" /> </li>
                                    </xsl:for-each>
                                </ul>
                            </xsl:when>
                            <xsl:otherwise>none</xsl:otherwise>
                        </xsl:choose>
                    </div>
                </div>
            </div>
        </div>

    </xsl:template>

    <xsl:template match="span">
        <b><xsl:value-of select="@name"/>: </b> <xsl:value-of select="." /> <br /> <xsl:if test="@newline='1'"><br /></xsl:if>
    </xsl:template>
</xsl:stylesheet>