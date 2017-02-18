<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width">
  <title>${name}'s tokens from ${start} to ${end}</title>
</head>
<body>
  <h2 style="text-align: center">${name}'s tokens from ${start} to ${end}</h2>
  <script src="/js/d3.min.js" type="text/javascript" charset="utf-8"></script>
  <script src="/js/chart.js" type="text/javascript" charset="utf-8"></script>
  <script>
    render("${name}", "${start}", "${end}");
  </script>
</body>
</html>
