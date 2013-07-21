spl : base [states] [contentAssistance] [generator] [extendedSudoku] :: _spl ;

contentAssistance : [undo] [color] [solver] :: _contentAssistance ;

%%

undo implies states ;
generator implies solver ;
solver implies undo ;

