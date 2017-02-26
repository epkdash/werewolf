db.Game.aggregate([
    {
        $match: {'settlement.ending': 'bloody'}
    },
    {
        $group: {
            _id: 'round[0][0].stage', total: {
                $sum: '$player'
            }
        }
    }
])

// {
//     $group: {
//         '_id': '$round.step.action', 'total': {
//             $sum: 1
//         }
//     }
// }


// db.Game.aggregate(
//     [
//         {
//             $unwind: '$player'
//         }
//         ,
//         {
//             $match: {$and: [{'player.name': 'maqinqin'}, {'player.role': 'witch'}, {'player.lovers': 'no'}]}
//         }
//         ,
//         {
//             $addFields: {
//                 player: '$player'
//             }
//         }
//     ])
//
//
// {
//     $group: {
//         _id: '$_id',
//             player:{
//                 $push: {
//                     'name':'$player.name', 'role':'$player.role', 'lovers':'$player.lovers', 'number':'$player.number'
//                 }
//             }
//     }
// }


db.Game.mapReduce(
    {
        query: {
            'settlement.survival': {$in: ['9']}
        }
    }
)


//统计狼人冠军榜
//按单个人统计 最后计算结果比较得出
// 卜文超 游戏胜利次数
db.Game.mapReduce(
    function () {
        for (var winner of this.settlement.winner) {
            if (winner == 'buwenchao') {
                emit(winner.name, 1);
            }
        }
    },
    function (key, value) {
        return Array.sum(value);
    },
    {
        out: 'count_buwenchao_win'
    }
)
// 卜文超 狼人胜利次数
db.Game.mapReduce(
    function () {
        for (var p of this.player) {
            if (p.role == 'wolf' && p.name == 'buwenchao') {
                emit(p.name, 1);
            }
        }
    },
    function (key, value) {
        return Array.sum(value);
    },
    {
        query: {'settlement.ending': 'bloody'},
        out: 'count_buwenchao_wolf_win'
    }
)
// 卜文超 狼人游戏次数
db.Game.mapReduce(
    function () {
        emit('buwenchao', 1);
    },
    function (key, value) {
        return Array.sum(value);
    },
    {
        query: {'player': {$elemMatch: {'name': 'buwenchao', 'role': 'wolf', 'lovers': 'no'}}},
        out: 'count_buwenchao_wolf'
    }
)

// 卜文超 参与游戏次数
db.Game.mapReduce(
    function () {
        emit('buwenchao', 1);
    },
    function (key, value) {
        return Array.sum(value);
    },
    {
        query: {'player': {$elemMatch: {'name': 'buwenchao'}}},
        out: 'count_buwenchao_join_game'
    }
)

//马勤勤 各个角色游戏次数
db.Game.aggregate(
    {
        $project: {
            'player': {
                $filter: {
                    input: '$player',
                    as: 'p',
                    cond: {$eq: ['$$p.name', 'maqinqin']}
                }

            }
        }
    },
    {
        $unwind: '$player'
    },
    {
        $group: {
            _id: '$player.role', total: {
                $sum: 1
            }
        }
    }
)


//马勤勤各个角色胜利次数
db.Game.aggregate(
    {
        $project: {
            'player': {
                $filter: {
                    input: '$player',
                    as: 'p',
                    cond: {$eq: ['$$p.name', 'maqinqin']}
                }
            },
            'settlement': 1
        }
    },
    {
        $unwind: '$player'
    },
    {
        $unwind: '$settlement.winner'
    },
    {
        $match: {'settlement.winner': 'maqinqin'}
    },
    {
        $group: {
            _id: '$player.role', total: {
                $sum: 1
            }
        }
    }
)


//统计女巫冠军榜
//按单个人统计 最后计算结果比较得出
//马勤勤女巫使用解药的次数
db.Game.aggregate(
    [
        {
            $project: {
                'player': {
                    $filter: {
                        input: '$player',
                        as: 'p',
                        cond: {
                            $and: [
                                {$eq: ['$$p.name', 'maqinqin']},
                                {$eq: ['$$p.role', 'witch']}
                            ]
                        }
                    }

                },
                'round': 1
            }
        },
        {
            $project: {
                'player': 1,
                'round': 1,
                'size': {$size: '$player'}
            }
        },
        {
            $match: {'size': {$gt: 0}}
        },
        {
            $unwind: '$round'
        },
        {
            $unwind: '$round'
        },
        {
            $unwind: '$round.step'
        },
        {
            $match: {$and: [{'round.stage': 'night'}, {'round.step.action': 'revive'}]}
        },
        {
            $count: 'total'
        }
    ]
)


//马勤勤女巫正确使用解药的次数（自己非情侣的时候，解药用在好人身上）
db.Game.aggregate(
    {
        $project: {
            'player': 1,
            'round': 1,
            'filter_player': {
                $filter: {
                    input: '$player',
                    as: 'p',
                    cond: {
                        $and: [
                            {$eq: ['$$p.name', 'maqinqin']},
                            {$eq: ['$$p.role', 'witch']},
                            {$eq: ['$$p.lovers', 'no']}
                        ]
                    }
                }
            }
        }
    },
    {
        $project: {
            'player': 1,
            'round': 1,
            'size': {$size: '$filter_player'}
        }
    },
    {
        $match: {'size': {$gt: 0}}
    },
    {
        $unwind: '$round'
    },
    {
        $unwind: '$round'
    },
    {
        $unwind: '$round.step'
    },
    {
        $match: {$and: [{'round.stage': 'night'}, {'round.step.action': 'revive'}]}
    },
    {
        $unwind: '$player'
    },
    {
        $unwind: '$round.step.passive'
    },
    {
        $redact: {
            $cond: {
                if: {$eq: ['$player.name', '$round.step.passive']},
                then: '$$DESCEND',
                else: '$$PRUNE'
            }
        }
    },
    {
        $match: {
            $and: [
                {'player.role': {$not: {$eq: 'wolf'}}},
                {'player.role': {$not: {$eq: 'whiteWolf'}}}
            ]
        }
    },
    {
        $count: 'total'
    }
)
//马勤勤女巫使用毒药的次数
db.Game.aggregate(
    [
        {
            $project: {
                'player': {
                    $filter: {
                        input: '$player',
                        as: 'p',
                        cond: {
                            $and: [
                                {$eq: ['$$p.name', 'maqinqin']},
                                {$eq: ['$$p.role', 'witch']}
                            ]
                        }
                    }

                },
                'round': 1
            }
        },
        {
            $project: {
                'player': 1,
                'round': 1,
                'size': {$size: '$player'}
            }
        },
        {
            $match: {'size': {$gt: 0}}
        },
        {
            $unwind: '$round'
        },
        {
            $unwind: '$round'
        },
        {
            $unwind: '$round.step'
        },
        {
            $match: {$and: [{'round.stage': 'night'}, {'round.step.action': 'poison'}]}
        },
        {
            $count: 'total'
        }
    ]
)
//马勤勤女巫正确使用毒药的次数（自己非情侣的时候，毒药用在坏人身上）
db.Game.aggregate(
    {
        $project: {
            'player': 1,
            'round': 1,
            'filter_player': {
                $filter: {
                    input: '$player',
                    as: 'p',
                    cond: {
                        $and: [
                            {$eq: ['$$p.name', 'maqinqin']},
                            {$eq: ['$$p.role', 'witch']},
                            {$eq: ['$$p.lovers', 'no']}
                        ]
                    }
                }
            }
        }
    },
    {
        $project: {
            'player': 1,
            'round': 1,
            'size': {$size: '$filter_player'}
        }
    },
    {
        $match: {'size': {$gt: 0}}
    },
    {
        $unwind: '$round'
    },
    {
        $unwind: '$round'
    },
    {
        $unwind: '$round.step'
    },
    {
        $match: {$and: [{'round.stage': 'night'}, {'round.step.action': 'poison'}]}
    },
    {
        $unwind: '$player'
    },
    {
        $unwind: '$round.step.passive'
    },
    {
        $redact: {
            $cond: {
                if: {$eq: ['$player.name', '$round.step.passive']},
                then: '$$DESCEND',
                else: '$$PRUNE'
            }
        }
    },
    {
        $match: {
            $or: [
                {'player.role': {$eq: 'wolf'}},
                {'player.role': {$eq: 'whiteWolf'}}
            ]

        }
    },
    {
        $count: 'total'
    }
)


//向阳作为预言家 活到游戏结束并且好人胜利的次数
db.Game.aggregate(
    {
        $project: {
            'player': 1,
            'settlement': 1,
            'filter_player': {
                $filter: {
                    input: '$player',
                    as: 'p',
                    cond: {
                        $and: [
                            {$eq: ['$$p.name', 'xiangyang']},
                            {$eq: ['$$p.role', 'seer']},
                            {$eq: ['$$p.lovers', 'no']}
                        ]
                    }
                }
            }
        }
    },
    {
        $project: {
            'player': 1,
            'settlement': 1,
            'size': {$size: '$filter_player'}
        }
    },
    {
        $match: {'size': {$gt: 0}}
    },
    {
        $match: {'settlement.ending': 'safety'}
    },
    {
        $unwind: '$settlement.survival'
    },
    {
        $match: {'settlement.survival': 'xiangyang'}
    },
    {
        $count: 'total'
    }
)


//马勤勤作为普通村民被杀次数
db.Game.aggregate(
    {
        $project: {
            'player': {
                $filter: {
                    input: '$player',
                    as: 'p',
                    cond: {
                        $and: [
                            {$eq: ['$$p.name', 'maqinqin']},
                            {$eq: ['$$p.role', 'villager']}
                        ]
                    }
                }
            },
            'settlement': 1
        }
    },
    {
        $unwind: '$player'
    },
    {
        $unwind: '$settlement.survival'
    },
    {
        $match: {'settlement.winner': {$not: {$eq: 'maqinqin'}}}
    },
    {
        $project: {
            'player': 1
        }
    },
    {
        $group: {
            _id: '$_id', total: {
                $sum: 1
            }
        }
    },
    {
        $count: 'total'
    }
)

//马勤勤作为预言家在第一晚被杀的次数
db.Game.aggregate(
    {
        $project: {
            'player': {
                $filter: {
                    input: '$player',
                    as: 'p',
                    cond: {
                        $and: [
                            {$eq: ['$$p.name', 'xiangyang']},
                            {$eq: ['$$p.role', 'seer']}
                        ]
                    }
                }
            },
            'round': 1
        }
    },
    {
        $unwind: '$player'
    },
    {
        $unwind: {path: "$round", includeArrayIndex: "arrayIndex"}
    },
    {
        $match: {'arrayIndex': 0}
    },
    {
        $unwind: '$round'
    },
    {
        $match: {'round.stage': 'night'}
    },
    {
        $unwind: '$round.step'
    },
    {
        $match: {'round.step.action': 'murder'}
    },
    {
        $unwind: '$round.step.passive'
    },
    {
        $match: {'round.step.passive': 'xiangyang'}
    }
)


//玩家被猎人带走的次数
db.Game.aggregate(
    {
        $project: {
            'player': {
                $filter: {
                    input: '$player',
                    as: 'p',
                    cond: {
                        $and: [
                            {$eq: ['$$p.name', 'zhuangbin']}
                        ]
                    }
                }
            },
            'round': 1
        }
    },
    {
        $unwind: '$player'
    },
    {
        $unwind: '$round'
    },
    {
        $unwind: '$round'
    },
    {
        $match: {'round.stage': 'day'}
    },
    {
        $unwind: '$round.step'
    },
    {
        $match: {'round.step.action': 'hunt'}
    },
    {
        $unwind: '$round.step.passive'
    },
    {
        $match: {'round.step.passive': 'zhuangbin'}
    }
)


//作为情侣最多次数的两位玩家
db.Game.aggregate(
    {
        $project: {'player': 1}
    },
    {
        $unwind: '$player'
    },
    {
        $match: {'player.lovers': 'yes'}
    },

    {
        $sort: {'player.name': -1}
    },
    {
        $group: {
            _id: '$_id', 'player': {$push: "$player.name"}
        }
    },
    {
        $group: {
            _id: '$player', total: {
                $sum: 1
            }
        }
    },
    {
        $sort: {'total': -1}
    },
    {
        $limit: 1
    }
)


//玩家被其他人以某方式伤害的次数统计
db.Game.aggregate(
    {
        $match: {'player.name': 'maqinqin'}
    },
    {
        $project: {
            'round': 1
        }
    },
    {
        $unwind: '$round'
    },
    {
        $unwind: '$round'
    },
    {
        $project: {
            'step': '$round.step'
        }
    },
    {
        $unwind: '$step'
    },
    {
        $match: {
            $or: [
                {'step.action': 'murder'},
                {'step.action': 'poison'},
                {'step.action': 'hunt'},
                {'step.action': 'implicate'}
            ]
        }
    },
    {
        $unwind: '$step.passive'
    },
    {
        $match: {'step.passive': 'maqinqin'}
    },
    {
        $unwind: '$step.initiative'
    },
    {
        $project: {
            'action': '$step.action',
            'initiative': '$step.initiative',
            'passive': '$step.passive'
        }
    },
    {
        $group: {
            _id: {passive:'$passive', initiative: '$initiative', action: '$action'},total: {
                $sum: 1
            }
        }
    },
    {
        $sort: {'total': -1}
    }
)


//玩家被某人伤害的次数最多
db.Game.aggregate(
    {
        $match: {'player.name': 'maqinqin'}
    },
    {
        $project: {
            'round': 1
        }
    },
    {
        $unwind: '$round'
    },
    {
        $unwind: '$round'
    },
    {
        $project: {
            'step': '$round.step'
        }
    },
    {
        $unwind: '$step'
    },
    {
        $match: {
            $or: [
                {'step.action': 'murder'},
                {'step.action': 'poison'},
                {'step.action': 'hunt'},
                {'step.action': 'implicate'}
            ]
        }
    },
    {
        $unwind: '$step.passive'
    },
    {
        $match: {'step.passive': 'maqinqin'}
    },
    {
        $unwind: '$step.initiative'
    },
    {
        $project: {
            'action': '$step.action',
            'initiative': '$step.initiative',
            'passive': '$step.passive'
        }
    },
    {
        $group: {
            _id: {passive:'$passive', initiative: '$initiative'},total: {
                $sum: 1
            }
        }
    },
    {
        $sort: {'total': -1}
    },
    {
        $limit: 1
    }
)
